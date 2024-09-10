//package com.example.expensify;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.example.expensify.databinding.ActivityAddExpenseBinding;
//import com.google.firebase.Firebase;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.Calendar;
//import java.util.UUID;
//
//public class AddExpenseActivity extends AppCompatActivity {
//
//    ActivityAddExpenseBinding binding;
//    private String type;
//    private ExpenseModel expenseModel;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityAddExpenseBinding.inflate(getLayoutInflater());
//        EdgeToEdge.enable(this);
//        setContentView(binding.getRoot());
//
//
//        type = getIntent().getStringExtra("type");
//        expenseModel = (ExpenseModel) getIntent().getSerializableExtra("model");
//
//        if(type==null) {
//            type = expenseModel.getType();
//            binding.amount.setText(String.valueOf(expenseModel.getAmount()));
//            binding.category.setText(expenseModel.getCategory());
//            binding.note.setText(expenseModel.getNote());
//        }
//
//        if(type.equals("Income")){
//            binding.incomeRadio.setChecked(true);
//        }
//        else{
//            binding.expenseRadio.setChecked(true);
//        }
//
//        binding.incomeRadio.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                type="Income";
//            }
//        });
//
//        binding.expenseRadio.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                type="Expense";
//            }
//        });
//
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater= getMenuInflater();
//        if(type.equals("income") || type.equals("expense")){
//            menuInflater.inflate(R.menu.add_menu,menu);
//        }
//        else{
//            menuInflater.inflate(R.menu.update_menu,menu);
//        }
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if(id == R.id.saveExpense){
//            if(type.equals("income") || type.equals("expense")){
//                createExpense();
//            }
//            else{
//                updateExpense();
//            }
//
//            return true;
//        }
//        if(id==R.id.deleteExpense){
//            deleteExpense();
//        }
//        return false;
//    }
//
//    private void deleteExpense(){
//        String userId = FirebaseAuth.getInstance().getUid();
//
//        if (userId == null) {
//            Toast.makeText(AddExpenseActivity.this, "User is not logged in", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        FirebaseFirestore
//                .getInstance()
//                .collection("users") // Access the "users" collection
//                .document(userId) // Get the document for the logged-in user's UID
//                .collection("expenses") // Access the "expenses" sub-collection
//                .document(expenseModel.getExpenseId()) // Specify the document by the expense's unique ID
//                .delete() // Delete the specific expense
//                .addOnSuccessListener(aVoid -> {
//                    Toast.makeText(AddExpenseActivity.this, "Expense deleted successfully!", Toast.LENGTH_SHORT).show();
//                    finish(); // Close the activity after deletion
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(AddExpenseActivity.this, "Failed to delete expense: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//    }
//
//
////    private void createExpense(){
////
////        FirebaseAuth auth = FirebaseAuth.getInstance();
////        FirebaseUser user = auth.getCurrentUser();
////
////        String expenseId = user.getUid();
////        String amount = binding.amount.getText().toString();
////        String note = binding.note.getText().toString();
////        String category = binding.category.getText().toString();
////
////        boolean incomeChecked = binding.incomeRadio.isChecked();
////
////        if(incomeChecked){
////            type="Income";
////        }
////        else{
////            type="Expense";
////        }
////        if(amount.trim().length()==0){
////            binding.amount.setError("Empty");
////            return;
////        }
////        ExpenseModel expenseModel = new ExpenseModel(expenseId,note,category,type,Long.parseLong(amount), Calendar.getInstance().getTimeInMillis(),FirebaseAuth.getInstance().getUid());
////
////        FirebaseFirestore
////                .getInstance()
////                .collection("expense")
////                .document(expenseId)
////                .set(expenseModel);
////        finish();
////    }
//
////    private void createExpense() {
////        FirebaseAuth auth = FirebaseAuth.getInstance();
////        FirebaseUser user = auth.getCurrentUser();
////
////        // Check if user is null (not logged in)
////        if (user == null) {
////            Log.e("FirestoreError", "User not logged in");
////            return;
////        }
////
////        String uid = user.getUid(); // Get the current user's UID
////        String amount = binding.amount.getText().toString();
////        String note = binding.note.getText().toString();
////        String category = binding.category.getText().toString();
////
////        boolean incomeChecked = binding.incomeRadio.isChecked();
////        String type = incomeChecked ? "Income" : "Expense";
////
////        // Validate input amount
////        if (amount.trim().isEmpty()) {
////            binding.amount.setError("Empty");
////            return;
////        }
////
////        // Create a new ExpenseModel object with the user's UID and other details
////        ExpenseModel expenseModel = new ExpenseModel(
////                uid, note, category, type,
////                Long.parseLong(amount),
////                Calendar.getInstance().getTimeInMillis(),
////                uid
////        );
////
////        // Firestore: Store the expense data under "users/{uid}/expenses/{expenseId}"
////        FirebaseFirestore
////                .getInstance()
////                .collection("users")
////                .document(uid)  // Reference to the logged-in user's document
////                .collection("expenses") // Sub-collection for expenses
////                .add(expenseModel) // Add new document with generated ID
////                .addOnSuccessListener(documentReference -> {
////                    Log.d("FirestoreSuccess", "Expense added with ID: " + documentReference.getId());
////                    // Optionally show a success message
////                    Toast.makeText(this, "Expense added!", Toast.LENGTH_SHORT).show();
////                    finish();
////                })
////                .addOnFailureListener(e -> {
////                    Log.e("FirestoreError", "Error adding expense", e);
////                    // Optionally show an error message
////                    Toast.makeText(this, "Failed to add expense", Toast.LENGTH_SHORT).show();
////                });
////    }
//
//    private void createExpense() {
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        FirebaseUser user = auth.getCurrentUser();
//
//        // Check if the user is null (not logged in)
//        if (user == null) {
//            Log.e("FirestoreError", "User not logged in");
//            return;
//        }
//
//        String uid = user.getUid(); // Get the current user's UID
//        String amount = binding.amount.getText().toString();
//        String note = binding.note.getText().toString();
//        String category = binding.category.getText().toString();
//
//        boolean incomeChecked = binding.incomeRadio.isChecked();
//        String type = incomeChecked ? "Income" : "Expense";
//
//        // Validate input amount
//        if (amount.trim().isEmpty()) {
//            binding.amount.setError("Empty");
//            return;
//        }
//
//        // Create a new ExpenseModel object with the user's UID and other details
//        String expenseId = UUID.randomUUID().toString(); // Generate a unique ID for the expense
//        ExpenseModel expenseModel = new ExpenseModel(
//                expenseId, note, category, type,
//                Long.parseLong(amount),
//                Calendar.getInstance().getTimeInMillis(),
//                uid // Set the UID to the current user
//        );
//
//        // Firestore: Store the expense data under "users/{uid}/expenses/{expenseId}"
//        FirebaseFirestore
//                .getInstance()
//                .collection("users") // Reference to the "users" collection
//                .document(uid) // Reference to the logged-in user's document
//                .collection("expenses") // Sub-collection for expenses
//                .document(expenseId) // Use the generated expenseId as the document ID
//                .set(expenseModel) // Set the expense data
//                .addOnSuccessListener(documentReference -> {
//                    Log.d("FirestoreSuccess", "Expense added with ID: " + expenseId);
//                    // Optionally show a success message
//                    Toast.makeText(this, "Expense added!", Toast.LENGTH_SHORT).show();
//                    finish();
//                })
//                .addOnFailureListener(e -> {
//                    Log.e("FirestoreError", "Error adding expense", e);
//                    // Optionally show an error message
//                    Toast.makeText(this, "Failed to add expense", Toast.LENGTH_SHORT).show();
//                });
//    }
//
//
//
//
////    private void updateExpense()
////    {
////
////        String expenseId = expenseModel.getExpenseId();
////        String amount = binding.amount.getText().toString();
////        String note = binding.note.getText().toString();
////        String category = binding.category.getText().toString();
////
////        boolean incomeChecked = binding.incomeRadio.isChecked();
////
////        if(incomeChecked){
////            type="Income";
////        }
////        else{
////            type="Expense";
////        }
////        if(amount.trim().length()==0){
////            binding.amount.setError("Empty");
////            return;
////        }
////        ExpenseModel model = new ExpenseModel(expenseId,note,category,type,Long.parseLong(amount), expenseModel.getTime() ,FirebaseAuth.getInstance().getUid());
////
////        FirebaseFirestore
////                .getInstance()
////                .collection("expense")
////                .document(expenseId)
////                .set(model);
////        finish();
////    }
//
//    private void updateExpense() {
//
//        String expenseId = expenseModel.getExpenseId(); // This should be the auto-generated expense document ID
//        String amount = binding.amount.getText().toString();
//        String note = binding.note.getText().toString();
//        String category = binding.category.getText().toString();
//
//        boolean incomeChecked = binding.incomeRadio.isChecked();
//
//        if (incomeChecked) {
//            type = "Income";
//        } else {
//            type = "Expense";
//        }
//
//        if (amount.trim().length() == 0) {
//            binding.amount.setError("Empty");
//            return;
//        }
//
//        // Create a new model with updated values
//        ExpenseModel model = new ExpenseModel(
//                expenseId,
//                note,
//                category,
//                type,
//                Long.parseLong(amount),
//                expenseModel.getTime(), // Keep the original time
//                FirebaseAuth.getInstance().getUid() // The user's UID
//        );
//
//        // Update the specific expense document inside the "expenses" sub-collection
//        FirebaseFirestore
//                .getInstance()
//                .collection("users") // Navigate to "users" collection
//                .document(FirebaseAuth.getInstance().getUid()) // Navigate to the logged-in user's document using their UID
//                .collection("expenses") // Access the "expenses" sub-collection
//                .document(expenseId) // The specific expense document to be updated (by expenseId)
//                .set(model) // Set the updated model
//                .addOnSuccessListener(aVoid -> {
//                    // Successfully updated the expense
//                    Toast.makeText(AddExpenseActivity.this, "Expense updated successfully!", Toast.LENGTH_SHORT).show();
//                    finish(); // Close the activity after updating
//                })
//                .addOnFailureListener(e -> {
//                    // Handle any errors
//                    Toast.makeText(AddExpenseActivity.this, "Failed to update expense: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//    }
//
//}

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

    private void createExpense() {
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
        ExpenseModel expenseModel = new ExpenseModel(
                expenseId, note, category, type,
                Long.parseLong(amount),
                Calendar.getInstance().getTimeInMillis(),
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

        ExpenseModel model = new ExpenseModel(
                expenseId, note, category, type,
                Long.parseLong(amount),
                expenseModel.getTime(),
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
