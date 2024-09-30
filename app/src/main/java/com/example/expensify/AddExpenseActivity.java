package com.example.expensify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expensify.databinding.ActivityAddExpenseBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.UUID;

public class AddExpenseActivity extends AppCompatActivity {

    ActivityAddExpenseBinding binding;
    private String type;
    private ExpenseModel expenseModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddExpenseBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        // Retrieving intent data
        type = getIntent().getStringExtra("type");
        expenseModel = (ExpenseModel) getIntent().getSerializableExtra("model");

        // Update screen if an existing expense is passed
        if (expenseModel != null) {
            type = expenseModel.getType();
            binding.amount.setText(String.valueOf(expenseModel.getAmount()));
            binding.category.setText(expenseModel.getCategory());
            binding.note.setText(expenseModel.getNote());

            if (type.equals("Income")) {
                binding.incomeRadio.setChecked(true);
            } else {
                binding.expenseRadio.setChecked(true);
            }
        }

        // Radio button listeners
        binding.incomeRadio.setOnClickListener(view -> type = "Income");
        binding.expenseRadio.setOnClickListener(view -> type = "Expense");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        if (expenseModel != null) {
            menuInflater.inflate(R.menu.update_menu, menu); // Show update menu when updating an existing expense
        } else {
            menuInflater.inflate(R.menu.add_menu, menu); // Show add menu when adding a new expense
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.saveExpense) {
            if (expenseModel != null) {
                updateExpense(); // Update an existing expense
            } else {
                createExpense(); // Create a new expense
            }
            return true;
        }
        if (id == R.id.deleteExpense) {
            deleteExpense(); // Delete an existing expense
            return true;
        }
        return false;
    }

    private void deleteExpense() {
        String userId = FirebaseAuth.getInstance().getUid();

        if (userId == null) {
            Toast.makeText(AddExpenseActivity.this, "User is not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(userId)
                .collection("expenses")
                .document(expenseModel.getExpenseId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddExpenseActivity.this, "Expense deleted successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddExpenseActivity.this, "Failed to delete expense: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void createExpense()
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Log.e("FirestoreError", "User not logged in");
            return;
        }

        String uid = user.getUid();
        String amount = binding.amount.getText().toString();
        String note = binding.note.getText().toString();
        String category = binding.category.getText().toString();

        boolean incomeChecked = binding.incomeRadio.isChecked();
        String type = incomeChecked ? "Income" : "Expense";

        if (amount.trim().isEmpty()) {
            binding.amount.setError("Empty");
            return;
        }

        String expenseId = UUID.randomUUID().toString();
        long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();

        ExpenseModel expenseModel = new ExpenseModel(
                expenseId, note, category, type,
                Long.parseLong(amount),
                currentTimeInMillis,
                uid
        );

        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(uid)
                .collection("expenses")
                .document(expenseId)
                .set(expenseModel)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Expense added!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add expense", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateExpense() {
        String expenseId = expenseModel.getExpenseId();
        String amount = binding.amount.getText().toString();
        String note = binding.note.getText().toString();
        String category = binding.category.getText().toString();

        boolean incomeChecked = binding.incomeRadio.isChecked();
        type = incomeChecked ? "Income" : "Expense";

        if (amount.trim().isEmpty()) {
            binding.amount.setError("Empty");
            return;
        }
        long time = expenseModel.getTime();

        ExpenseModel model = new ExpenseModel(
                expenseId, note, category, type,
                Long.parseLong(amount),
                time,
                FirebaseAuth.getInstance().getUid()
        );

        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("expenses")
                .document(expenseId)
                .set(model)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddExpenseActivity.this, "Expense updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddExpenseActivity.this, "Failed to update expense: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}