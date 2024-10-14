package com.example.expensify;

public class Category {
    private String name;
    private int iconResId;

    public Category(String name, int iconResId) {
        this.name = name;
        this.iconResId = iconResId;
    }

    public String getName() {
        return name;
    }

    public int getIconResId() {
        return iconResId;
    }

    @Override
    public String toString() {
        return name; // This ensures the category name is displayed in the spinner
    }
}
