package com.example.expensify;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class CategoryExpenseActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExpensesAdapter expensesAdapter;
    private String categoryName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_expense);

        // Get category name from Intent
        categoryName = getIntent().getStringExtra("categoryName");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        expensesAdapter = new ExpensesAdapter(this, expenseModel -> {
            // Handle item click if needed
        });
        recyclerView.setAdapter(expensesAdapter);

        fetchExpenses();
    }

    private void fetchExpenses()
    {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return;

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("expenses")
                .whereEqualTo("category", categoryName)
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ExpenseModel> expenseList = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                        ExpenseModel model = snapshot.toObject(ExpenseModel.class);
                        if (model != null) {
                            expenseList.add(model);
                        }
                    }
                    expensesAdapter.setExpenseList(expenseList);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CategoryExpenseActivity.this, "Failed to fetch expenses: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onBackPressed() {
        // Simply finish the current activity to go back to the previous fragment
        super.onBackPressed();
        finish();
    }
}
