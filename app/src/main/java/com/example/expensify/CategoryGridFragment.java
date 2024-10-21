package com.example.expensify;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class CategoryGridFragment extends Fragment {

    private GridView gridView;
    private List<Category> categories;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_category_grid, container, false);
        gridView = view.findViewById(R.id.categoryGridView);

        // Prepare categories
        categories = new ArrayList<>();
        categories.add(new Category("Food", R.drawable.icon1));
        categories.add(new Category("Transport", R.drawable.icon2));
        categories.add(new Category("Household", R.drawable.icon3));
        categories.add(new Category("Cosmetics", R.drawable.icon4));
        categories.add(new Category("Cloth", R.drawable.icon5));
        categories.add(new Category("Education", R.drawable.icon6));
        categories.add(new Category("Health", R.drawable.icon7));
        categories.add(new Category("Other", R.drawable.icon8));

        CategoryAdapter categoryAdapter = new CategoryAdapter(requireContext(), categories);
        gridView.setAdapter(categoryAdapter);

        // Set item click listener
        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            Category clickedCategory = categories.get(position);
            Intent intent = new Intent(requireActivity(), CategoryExpenseActivity.class);
            intent.putExtra("categoryName", clickedCategory.getName());
            startActivity(intent);
        });

        return view;
    }
}
