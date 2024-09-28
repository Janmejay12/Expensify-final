package com.example.expensify;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;

public class MonthlyExpenseActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private FirebaseFirestore firestore;
    private Button previousMonthButton, nextMonthButton,goDash ;
    Intent intent;
    private Calendar currentCalendar; // To keep track of the displayed month

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_expense);

        calendarView = findViewById(R.id.calendarView);
        previousMonthButton = findViewById(R.id.previousMonthButton);
        goDash = findViewById(R.id.goDashBoard);
        nextMonthButton = findViewById(R.id.nextMonthButton);
        firestore = FirebaseFirestore.getInstance();
        currentCalendar = Calendar.getInstance();

        // Set up calendar listener
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Log.d("CalendarView", "Selected date: " + year + "-" + (month + 1) + "-" + dayOfMonth);
            currentCalendar.set(year, month, dayOfMonth);
            long selectedDate = currentCalendar.getTimeInMillis(); // Get time in milliseconds

            // Fetch data from Firestore for the selected date
            fetchExpensesForDay(selectedDate);
        });

        // Set up previous month button listener
        previousMonthButton.setOnClickListener(view -> navigateToPreviousMonth());

        // Set up next month button listener
        nextMonthButton.setOnClickListener(view -> navigateToNextMonth());

        goDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MonthlyExpenseActivity.this,DashBoard2.class);
                startActivity(intent);
                finish();
            }
        });
    }

//    private void fetchExpensesForDay(long selectedDate) {
//        // Convert selected date to start and end of the day (in milliseconds)
//        Calendar startOfDay = Calendar.getInstance();
//        startOfDay.setTimeInMillis(selectedDate);
//        startOfDay.set(Calendar.HOUR_OF_DAY, 0);
//        startOfDay.set(Calendar.MINUTE, 0);
//        startOfDay.set(Calendar.SECOND, 0);
//
//        Calendar endOfDay = Calendar.getInstance();
//        endOfDay.setTimeInMillis(selectedDate);
//        endOfDay.set(Calendar.HOUR_OF_DAY, 23);
//        endOfDay.set(Calendar.MINUTE, 59);
//        endOfDay.set(Calendar.SECOND, 59);
//
//        long startTimestamp = startOfDay.getTimeInMillis();
//        long endTimestamp = endOfDay.getTimeInMillis();
//
//        // Query Firestore to fetch all expenses between start and end of the day
//        firestore.collection("expenses")
//                .whereGreaterThanOrEqualTo("time", startTimestamp)
//                .whereLessThanOrEqualTo("time", endTimestamp)
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    long totalIncome = 0;
//                    long totalExpense = 0;
//
//                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
//                        ExpenseModel expense = document.toObject(ExpenseModel.class);
//                        if ("Income".equals(expense.getType())) {
//                            totalIncome += expense.getAmount();
//                        } else {
//                            totalExpense += expense.getAmount();
//                        }
//                    }
//
//                    // Show the result in a dialog box
//                    showExpenseDialog(totalIncome, totalExpense);
//                })
//                .addOnFailureListener(e -> Toast.makeText(MonthlyExpenseActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show());
//    }

//    private void fetchExpensesForDay(long selectedDate) {
//        // Convert selected date to start and end of the day (in milliseconds)
//        Calendar startOfDay = Calendar.getInstance();
//        startOfDay.setTimeInMillis(selectedDate);
//        startOfDay.set(Calendar.HOUR_OF_DAY, 0);
//        startOfDay.set(Calendar.MINUTE, 0);
//        startOfDay.set(Calendar.SECOND, 0);
//
//        Calendar endOfDay = Calendar.getInstance();
//        endOfDay.setTimeInMillis(selectedDate);
//        endOfDay.set(Calendar.HOUR_OF_DAY, 23);
//        endOfDay.set(Calendar.MINUTE, 59);
//        endOfDay.set(Calendar.SECOND, 59);
//
//        long startTimestamp = startOfDay.getTimeInMillis();
//        long endTimestamp = endOfDay.getTimeInMillis();
//
//        // Logging the start and end timestamp to ensure they are correct
//        System.out.println("Start of day: " + startTimestamp);
//        System.out.println("End of day: " + endTimestamp);
//
//        // Query Firestore to fetch all expenses between start and end of the day
//        firestore.collection("expenses")
//                .whereGreaterThanOrEqualTo("time", startTimestamp)
//                .whereLessThanOrEqualTo("time", endTimestamp)
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    if (!queryDocumentSnapshots.isEmpty()) {
//                        long totalIncome = 0;
//                        long totalExpense = 0;
//
//                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
//                            ExpenseModel expense = document.toObject(ExpenseModel.class);
//
//                            // Log each retrieved document for debugging
//                            System.out.println("Expense found: " + document.getId());
//
//                            if ("Income".equals(expense.getType())) {
//                                totalIncome += expense.getAmount();
//                            } else {
//                                totalExpense += expense.getAmount();
//                            }
//                        }
//
//                        // Show the result in a dialog box
//                        showExpenseDialog(totalIncome, totalExpense);
//                    } else {
//                        Toast.makeText(MonthlyExpenseActivity.this, "No data found for this day.", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(MonthlyExpenseActivity.this, "Failed to fetch data: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                    e.printStackTrace();  // Log error to console for debugging
//                });
//    }

//    private void fetchExpensesForDay(long date) {
//        // Calendar instance for the selected day
//        Calendar calendarStart = Calendar.getInstance();
//        calendarStart.setTimeInMillis(date);
//
//        // Set the start of the day (00:00:00)
//        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
//        calendarStart.set(Calendar.MINUTE, 0);
//        calendarStart.set(Calendar.SECOND, 0);
//        calendarStart.set(Calendar.MILLISECOND, 0);
//        long startOfDay = calendarStart.getTimeInMillis();
//
//        // Set the end of the day (23:59:59)
//        Calendar calendarEnd = Calendar.getInstance();
//        calendarEnd.setTimeInMillis(date);
//        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
//        calendarEnd.set(Calendar.MINUTE, 59);
//        calendarEnd.set(Calendar.SECOND, 59);
//        calendarEnd.set(Calendar.MILLISECOND, 999);
//        long endOfDay = calendarEnd.getTimeInMillis();
//
//        // Log to debug if needed
//        Log.d("FirestoreQuery", "Start Time: " + startOfDay);
//        Log.d("FirestoreQuery", "End Time: " + endOfDay);
//
//        // Query Firestore for expenses on the selected day (using long timestamps)
//        firestore.collection("expenses")
//                .whereEqualTo("uid", FirebaseAuth.getInstance().getCurrentUser().getUid())  // Ensure you're querying for the current user's data
//                .whereGreaterThanOrEqualTo("time", startOfDay)
//                .whereLessThanOrEqualTo("time", endOfDay)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        QuerySnapshot querySnapshot = task.getResult();
//                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
//                            long totalIncome = 0;
//                            long totalExpense = 0;
//
//                            // Iterate through the results and sum up income and expenses
//                            for (DocumentSnapshot document : querySnapshot) {
//                                ExpenseModel expense = document.toObject(ExpenseModel.class);
//                                if (expense != null) {
//                                    if (expense.getType().equals("Income")) {
//                                        totalIncome += expense.getAmount();
//                                    } else if (expense.getType().equals("Expense")) {
//                                        totalExpense += expense.getAmount();
//                                    }
//                                }
//                            }
//
//                            // Display a dialog with the total income and total expense for the day
//                            showExpenseDialog(totalIncome, totalExpense);
//                        } else {
//                            // If no documents were found for the selected day
//                            Toast.makeText(MonthlyExpenseActivity.this, "No data found for this day", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        // Handle Firestore query failure
//                        Log.e("Firestore", "Error getting documents: ", task.getException());
//                    }
//                });
//    }

    private void fetchExpensesForDay(long date) {
        Log.d("MonthlyExpenseActivity", "Fetching expenses for date: " + date);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Check if the user is logged in
        if (currentUser == null) {
            // If not logged in, show a message or redirect to the login page
            Toast.makeText(MonthlyExpenseActivity.this, "User not logged in. Please sign in.", Toast.LENGTH_SHORT).show();
            // Optionally, redirect to login page
             Intent intent = new Intent(MonthlyExpenseActivity.this, MainActivity.class);
             startActivity(intent);
            return; // Exit the method as there's no user to fetch data for
        }

        String uid = currentUser.getUid(); // Get the user's UID

        // Proceed with fetching the expenses for the selected date
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

        firestore.collection("users")
                .document(uid) // Add this line if expenses are stored under a user's document
                .collection("expenses")
                .whereEqualTo("uid", uid)  // Use the retrieved UID
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
                            Toast.makeText(MonthlyExpenseActivity.this, "No data found for this day", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("Firestore", "Error getting documents: ", task.getException());
                    }
                });
    }





    private void showExpenseDialog(long totalIncome, long totalExpense) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
