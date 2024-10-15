package com.example.expensify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExpensesAdapter extends RecyclerView.Adapter <ExpensesAdapter.MyViewHolder> {

    private Context context;
    private OnItemsClick onItemsClick;
    private List<ExpenseModel> expenseModelList;

    public ExpensesAdapter(Context context,OnItemsClick onItemsClick) {
        this.context = context;
        expenseModelList = new ArrayList<>();
        this.onItemsClick = onItemsClick;
    }

    public void add(ExpenseModel expenseModel){
        expenseModelList.add(expenseModel);
        notifyDataSetChanged();
    }

    public void clear(){
        expenseModelList.clear();
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_row,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    ExpenseModel expenseModel = expenseModelList.get(position);
        holder.note.setText(expenseModel.getNote());
        holder.category.setText(expenseModel.getCategory());
        holder.amount.setText(String.valueOf(expenseModel.getAmount()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        String formattedDate = dateFormat.format(new Date(expenseModel.getTime()));
        holder.date.setText(formattedDate);

        // Set the category icon based on the category
        int iconResId = getCategoryIcon(expenseModel.getCategory());
        holder.categoryIcon.setImageResource(iconResId);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemsClick.onClick(expenseModel);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return expenseModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        private TextView note,category,amount,date;
        private ImageView categoryIcon;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            note = itemView.findViewById(R.id.note);
            category = itemView.findViewById(R.id.category);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            categoryIcon = itemView.findViewById(R.id.categoryIcon);  // Reference to category icon
        }
    }
    private int getCategoryIcon(String category) {
        switch (category) {
            case "Food":
                return R.drawable.icon1;
            case "Transport":
                return R.drawable.icon2;
            case "Household":
                return R.drawable.icon3;
            case "Cosmetics":
                return R.drawable.icon4;
            case "Cloth":
                return R.drawable.icon5;
            case "Education":
                return R.drawable.icon6;
            case "Health":
                return R.drawable.icon7;
            case "Other":
                return R.drawable.icon8;

            default:
                return R.drawable.icon1; // Default icon if category doesn't match
        }
    }
}
