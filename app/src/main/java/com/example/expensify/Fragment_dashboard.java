package com.example.expensify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.expensify.databinding.FragmentDashboardBinding;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class Fragment_dashboard extends Fragment implements OnItemsClick {

    private FragmentDashboardBinding binding;
    private ExpensesAdapter expensesAdapter;
    private long income = 0;
    private long expense = 0;
    private Intent intent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout and initialize the binding for the fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot(); // Return the root view for the fragment
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the adapter
        expensesAdapter = new ExpensesAdapter(requireContext(), this); // Pass 'this' for OnItemsClick
        binding.recycler.setAdapter(expensesAdapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(requireActivity()));

        // Initialize the Intent for adding expenses
        intent = new Intent(requireActivity(), AddExpenseActivity.class);

        // Set up listeners for buttons
        binding.addBtn.setOnClickListener(view12 -> {
            intent.removeExtra("model"); // Clear any previously set model
            startActivity(intent);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            ProgressDialog progressDialog = new ProgressDialog(requireActivity());
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Signing in anonymously...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            FirebaseAuth.getInstance()
                    .signInAnonymously()
                    .addOnSuccessListener(authResult -> progressDialog.dismiss())
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(requireActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        requireActivity().finish();
                    });
        } else {
            fetchData();
        }
    }

    // Fetch data from Firestore
    private void fetchData() {
        income = 0;
        expense = 0;

        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return;

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("expenses")
                .orderBy("time", Query.Direction.DESCENDING) // Order by 'time' field in descending order
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ExpenseModel> list = new ArrayList<>();
                    expensesAdapter.clear();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                        ExpenseModel model = snapshot.toObject(ExpenseModel.class);
                        if (model != null) {
                            list.add(model);
                            expensesAdapter.add(model);

                            if ("Income".equals(model.getType())) {
                                income += model.getAmount();
                            } else {
                                expense += model.getAmount();
                            }
                        }
                    }
                    setUpGraph(list);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(requireActivity(), "Error fetching data: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    // Set up the Pie chart graph
    // Set up the Pie chart graph
    private void setUpGraph(List<ExpenseModel> list) {
        if (!isAdded()) {
            return; // Ensure the fragment is attached to its activity
        }

        List<PieEntry> entries = new ArrayList<>();

        long totalIncome = 0;
        long totalExpense = 0;

        for (ExpenseModel model : list) {
            if ("Income".equals(model.getType())) {
                totalIncome += model.getAmount();
            } else {
                totalExpense += model.getAmount();
            }
        }

        if (totalIncome > 0) {
            entries.add(new PieEntry(totalIncome, "Income"));
            binding.ti.setText("Income:" + totalIncome);
        }
        if (totalExpense > 0) {
            entries.add(new PieEntry(totalExpense, "Expense"));
            binding.te.setText("Expense:" + totalExpense);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Income vs Expense");

        // Ensure the activity is accessible
        if (isAdded()) {
            dataSet.setColors(new int[]{R.color.yellow, R.color.red}, requireActivity());
        }

        PieData data = new PieData(dataSet);

        binding.pieChart.setData(data);
        binding.pieChart.invalidate();  // Refresh the chart
    }


    @Override
    public void onClick(ExpenseModel expenseModel) {
        // Start AddExpenseActivity with the selected expenseModel
        Intent intent = new Intent(requireActivity(), AddExpenseActivity.class);

        // Pass the expenseModel to the AddExpenseActivity (ExpenseModel must implement Serializable or Parcelable)
        intent.putExtra("model", expenseModel);

        // Start the AddExpenseActivity
        startActivity(intent);
    }
}
