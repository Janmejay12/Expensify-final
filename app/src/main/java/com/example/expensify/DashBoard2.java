//package com.example.expensify;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//
//import com.example.expensify.databinding.ActivityDashBoard2Binding;
//import com.example.expensify.databinding.ActivityMainBinding;
//import com.github.mikephil.charting.data.PieData;
//import com.github.mikephil.charting.data.PieDataSet;
//import com.github.mikephil.charting.data.PieEntry;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.api.Context;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class DashBoard2 extends AppCompatActivity implements OnItemsClick{
//
//
//    ActivityDashBoard2Binding binding;
//    private ExpensesAdapter expensesAdapter;
//    Intent intent;
//    private long income = 0;
//    private long expense = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityDashBoard2Binding.inflate(getLayoutInflater());
//        EdgeToEdge.enable(this);
//        setContentView(binding.getRoot());
//
//        expensesAdapter = new ExpensesAdapter(this,this);
//        binding.recycler.setAdapter(expensesAdapter);
//        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
//
//        intent = new Intent(DashBoard2.this,AddExpenseActivity.class);
//
//        binding.addIncome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                intent.putExtra("type","Income");
//                startActivity(intent);
//            }
//        });
//
//        binding.addExpense.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                intent.putExtra("type","Expense");
//                startActivity(intent);
//            }
//        });
//
//        binding.logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(DashBoard2.this,MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Please");
//        progressDialog.setMessage("wait");
//        progressDialog.setCancelable(false);
//
//        if(FirebaseAuth.getInstance().getCurrentUser()==null){
//            progressDialog.show();
//            FirebaseAuth.getInstance()
//                    .signInAnonymously()
//                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                        @Override
//                        public void onSuccess(AuthResult authResult) {
//                            progressDialog.cancel();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            progressDialog.cancel();
//                            Toast.makeText(DashBoard2.this,e.getMessage(),Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        income=0;
//        expense=0;
//
//        getData();
//    }
//
////    private void getData(){
////        FirebaseFirestore
////                .getInstance()
////                .collection("expense")
////                .whereEqualTo("uid",FirebaseAuth.getInstance().getUid())
////                .get()
////                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
////                    @Override
////                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
////                        expensesAdapter.clear();
////                        List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
////                        for(DocumentSnapshot ds:dsList){
////                            ExpenseModel expenseModel = ds.toObject(ExpenseModel.class);
////                            expensesAdapter.add(expenseModel);
////                        }
////                    }
////                });
////    }
//
////    private void getData() {
////        FirebaseFirestore
////                .getInstance()
////                .collection("users") // Access the "users" collection
////                .document(FirebaseAuth.getInstance().getUid()) // Access the document for the logged-in user's UID
////                .collection("expenses") // Access the "expenses" sub-collection
////                .get() // Get all documents in the "expenses" sub-collection
////                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
////                    @Override
////                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
////                        expensesAdapter.clear(); // Clear previous data from the adapter
////                        List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments(); // Get the list of expense documents
////                        for (DocumentSnapshot ds : dsList) {
////                            ExpenseModel expenseModel = ds.toObject(ExpenseModel.class); // Convert the document to ExpenseModel
////
////                            if(expenseModel.getType().equals("income")){
////                                income+=expenseModel.getAmount();
////                            }
////                            else{
////                                expense+=expenseModel.getAmount();
////                            }
////
////                            expensesAdapter.add(expenseModel); // Add the expense model to the adapter
////                        }
////                        setUpGraph();
////                    }
////                })
////                .addOnFailureListener(new OnFailureListener() {
////                    @Override
////                    public void onFailure(@NonNull Exception e) {
////                        Toast.makeText(DashBoard2.this, "Error fetching expenses: " + e.getMessage(), Toast.LENGTH_SHORT).show();
////                    }
////                });
////    }
//
//    private void getData() {
//        String uid = FirebaseAuth.getInstance().getUid(); // Fetch the UID of the logged-in user
//
//        if (uid == null) {
//            Toast.makeText(DashBoard2.this, "Error: User is not logged in", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        FirebaseFirestore
//                .getInstance()
//                .collection("users") // Access the "users" collection
//                .document(uid) // Access the document for the logged-in user's UID
//                .collection("expenses") // Access the "expenses" sub-collection
//                .get() // Get all documents in the "expenses" sub-collection
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        expensesAdapter.clear(); // Clear previous data from the adapter
//                        List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments(); // Get the list of expense documents
//                        for (DocumentSnapshot ds : dsList) {
//                            ExpenseModel expenseModel = ds.toObject(ExpenseModel.class); // Convert the document to ExpenseModel
//
//                            if (expenseModel.getType().equals("Income")) {
//                                income += expenseModel.getAmount();
//                            } else {
//                                expense += expenseModel.getAmount();
//                            }
//
//                            expensesAdapter.add(expenseModel); // Add the expense model to the adapter
//                        }
//                        setUpGraph(); // Update the graph with the new data
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(DashBoard2.this, "Error fetching expenses: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//
//    private void setUpGraph(){
//        List<PieEntry> pieEntryList = new ArrayList<>();
//        List<Integer>  colorsList = new ArrayList<>();
//        if(income!=0)
//        {
//            pieEntryList.add(new PieEntry(income,"Income"));
//            colorsList.add(getResources().getColor(R.color.yellow));
//        }
//        if(expense!=0)
//        {
//            pieEntryList.add(new PieEntry(expense,"Income"));
//            colorsList.add(getResources().getColor(R.color.red));
//        }
//        PieDataSet pieDataSet = new PieDataSet(pieEntryList , String.valueOf(income-expense));
//        pieDataSet.setColors(colorsList);
//        PieData pieData = new PieData(pieDataSet);
//
//
//        binding.pieChart.setData(pieData);
//        binding.pieChart.invalidate();
//
//    }
//
////    @Override
////    public void onClick(ExpenseModel expenseModel)
////    {
////        intent = new Intent(DashBoard2.this,AddExpenseActivity.class);
////        intent.putExtra("model",expenseModel);
////        startActivity(intent);
////    }
//
//    @Override
//    public void onClick(ExpenseModel expenseModel) {
//        if (expenseModel != null) {
//            Intent intent = new Intent(DashBoard2.this, AddExpenseActivity.class);
//            intent.putExtra("model", expenseModel);  // Make sure ExpenseModel implements Serializable
//            startActivity(intent);
//        } else {
//            Toast.makeText(DashBoard2.this, "Error: No expense model found!", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//}

package com.example.expensify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.expensify.databinding.ActivityDashBoard2Binding;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;



public class DashBoard2 extends AppCompatActivity implements OnItemsClick {

    ActivityDashBoard2Binding binding;
    private ExpensesAdapter expensesAdapter;
    private long income = 0;
    private long expense = 0;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashBoard2Binding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        // Initializing the adapter
        expensesAdapter = new ExpensesAdapter(this, this);
        binding.recycler.setAdapter(expensesAdapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));

        // Intent setup for adding income or expense
        intent = new Intent(DashBoard2.this, AddExpenseActivity.class);

        binding.addIncome.setOnClickListener(view -> {
            intent.removeExtra("model");  // Ensure no old expenseModel data is passed
            intent.putExtra("type", "Income");
            startActivity(intent);
        });

        binding.addExpense.setOnClickListener(view -> {
            intent.removeExtra("model");  // Ensure no old expenseModel data is passed
            intent.putExtra("type", "Expense");
            startActivity(intent);
        });

        // Logout functionality
        binding.logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(DashBoard2.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        binding.calender.setOnClickListener(view -> {

            Intent intent = new Intent(DashBoard2.this, MonthlyExpenseActivity.class);
            startActivity(intent);

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Signing in anonymously...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            FirebaseAuth.getInstance()
                    .signInAnonymously()
                    .addOnSuccessListener(authResult -> progressDialog.cancel())
                    .addOnFailureListener(e -> {
                        progressDialog.cancel();
                        Toast.makeText(DashBoard2.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    });
        } else {
            fetchData();
        }
    }

    // Fetch data when the activity starts
    private void fetchData() {
        income = 0;
        expense = 0;

        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return;

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("expenses")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ExpenseModel> list = new ArrayList<>();
                    expensesAdapter.clear();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                        ExpenseModel model = snapshot.toObject(ExpenseModel.class);
                        expensesAdapter.add(model);
                        if (model != null) {
                            list.add(model);
                            if (model.getType().equals("Income")) {
                                income += model.getAmount();
                            } else {
                                expense += model.getAmount();
                            }
                        }
                    }
                    setUpGraph(list);
                })
                .addOnFailureListener(e -> Toast.makeText(DashBoard2.this, "Error fetching data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Function to set up the graph
    private void setUpGraph(List<ExpenseModel> list) {
        List<PieEntry> entries = new ArrayList<>();

        long totalIncome = 0;
        long totalExpense = 0;

        for (ExpenseModel model : list) {
            if (model.getType().equals("Income")) {
                totalIncome += model.getAmount();
            } else {
                totalExpense += model.getAmount();
            }
        }

        if (totalIncome > 0) {
            entries.add(new PieEntry(totalIncome, "Income"));
        }
        if (totalExpense > 0) {
            entries.add(new PieEntry(totalExpense, "Expense"));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Income vs Expense");
        dataSet.setColors(new int[]{R.color.yellow, R.color.red}, this);
        PieData data = new PieData(dataSet);

        binding.pieChart.setData(data);
        binding.pieChart.invalidate(); // Refresh the chart
    }

    // Called when the activity resumes
    @Override
    protected void onResume() {
        super.onResume();
        fetchData(); // Refresh the data when the user returns to the dashboard
    }

    @Override
    public void onClick(ExpenseModel expenseModel) {
        // Pass the selected expense to the AddExpenseActivity for editing
        intent.putExtra("model", expenseModel);
        startActivity(intent);
    }
}