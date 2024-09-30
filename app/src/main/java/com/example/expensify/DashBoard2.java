package com.example.expensify;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class DashBoard2 extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board2);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        viewPager.setAdapter(new FragmentAdapter(DashBoard2.this));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setIcon(R.drawable.dashboard);
                    break;
                case 1:
                    tab.setIcon(R.drawable.calendar);
                    break;
                case 2:
                    tab.setIcon(R.drawable.more);
                    break;
            }
        }).attach();
    }

    private static class FragmentAdapter extends FragmentStateAdapter {
        public FragmentAdapter(DashBoard2 activity) {
            super(activity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new Fragment_dashboard();
                case 1:
                    return new FragmentMonthlyExpense();
                case 2:
                    return new Fragment_user();
                default:
                    return new Fragment_dashboard();
            }
        }

        @Override
        public int getItemCount() {
            return 3; // Number of tabs
        }
    }
}
