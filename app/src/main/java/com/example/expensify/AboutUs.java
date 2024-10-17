package com.example.expensify;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Button learnMoreButton = findViewById(R.id.learnMoreButton);
        learnMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add action for the "Learn More" button
                Toast.makeText(AboutUs.this, "Learn more about our services!", Toast.LENGTH_SHORT).show();
                // You can also navigate to another activity or open a website here
            }
        });
    }
}
