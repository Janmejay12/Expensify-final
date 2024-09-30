package com.example.expensify;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class FragmentMonthlyExpense extends Fragment {

    private CalendarView calendarView;
    private FirebaseFirestore firestore;
    private Button previousMonthButton, nextMonthButton, goDash;
    private Calendar currentCalendar; // To keep track of the displayed month
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_monthly_expense, container, false);

        // Initialize views
        calendarView = rootView.findViewById(R.id.calendarView);

        // Set up Firestore
        firestore = FirebaseFirestore.getInstance();
        currentCalendar = Calendar.getInstance();

        // Get the current Firebase user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Set up calendar listener
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Log.d("CalendarView", "Selected date: " + year + "-" + (month + 1) + "-" + dayOfMonth);
            currentCalendar.set(year, month, dayOfMonth);
            long selectedDate = currentCalendar.getTimeInMillis(); // Get time in milliseconds

            // Fetch data from Firestore for the selected date
            fetchExpensesForDay(selectedDate);
        });

        

        return rootView;
    }

    private void fetchExpensesForDay(long date) {
        Log.d("FragmentMonthlyExpense", "Fetching expenses for date: " + date);

        // Check if the user is logged in
        if (currentUser == null) {
            // If not logged in, show a message or redirect to the login page
            Toast.makeText(getActivity(), "User not logged in. Please sign in.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            return; // Exit the method as there's no user to fetch data for
        }

        String uid = currentUser.getUid(); // Get the user's UID

        // Set up time range for the selected day
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTimeInMillis(date);
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        calendarStart.set(Calendar.MILLISECOND, 0);
        long startOfDay = calendarStart.getTimeInMillis();

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTimeInMillis(date);
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 59);
        calendarEnd.set(Calendar.SECOND, 59);
        calendarEnd.set(Calendar.MILLISECOND, 999);
        long endOfDay = calendarEnd.getTimeInMillis();

        // Log the start and end of the day for debugging
        Log.d("Firestore", "Fetching data from " + startOfDay + " to " + endOfDay);

        // Firestore query for expenses within the selected day
        firestore.collection("users")
                .document(uid)
                .collection("expenses")
                .whereEqualTo("uid", uid)
                .whereGreaterThanOrEqualTo("time", startOfDay)
                .whereLessThanOrEqualTo("time", endOfDay)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            long totalIncome = 0;
                            long totalExpense = 0;

                            for (DocumentSnapshot document : querySnapshot) {
                                ExpenseModel expense = document.toObject(ExpenseModel.class);
                                if (expense != null) {
                                    if (expense.getType().equals("Income")) {
                                        totalIncome += expense.getAmount();
                                    } else if (expense.getType().equals("Expense")) {
                                        totalExpense += expense.getAmount();
                                    }
                                }
                            }

                            showExpenseDialog(totalIncome, totalExpense);
                        } else {
                            Toast.makeText(getActivity(), "No data found for this day", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("Firestore", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void showExpenseDialog(long totalIncome, long totalExpense) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Daily Summary");
        builder.setMessage("Total Income: " + totalIncome + "\nTotal Expense: " + totalExpense);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void navigateToPreviousMonth() {
        // Subtract one month from the current calendar
        currentCalendar.add(Calendar.MONTH, -1);
        updateCalendarView();
    }

    private void navigateToNextMonth() {
        // Add one month to the current calendar
        currentCalendar.add(Calendar.MONTH, 1);
        updateCalendarView();
    }

    private void updateCalendarView() {
        // Update the CalendarView to reflect the month change
        long newDate = currentCalendar.getTimeInMillis();
        calendarView.setDate(newDate, true, true); // Update calendar display
    }
}
