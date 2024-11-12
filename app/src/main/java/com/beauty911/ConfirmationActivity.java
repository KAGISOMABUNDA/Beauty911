package com.beauty911;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ConfirmationActivity extends AppCompatActivity {

    private TextView nameSurnameTextView, dateTimeTextView, servicesTextView, totalTextView;
    private Button backToHomeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        // Initialize TextViews
        nameSurnameTextView = findViewById(R.id.nameSurnameTextView);
        dateTimeTextView = findViewById(R.id.dateTimeTextView);
        servicesTextView = findViewById(R.id.servicesTextView);
        totalTextView = findViewById(R.id.totalTextView);

        // Initialize back button
        backToHomeButton = findViewById(R.id.backToHomeButton);

        // Get data passed from CheckoutActivity
        String name = getIntent().getStringExtra("name");
        String surname = getIntent().getStringExtra("surname");
        String dateTime = getIntent().getStringExtra("dateTime");
        double total = getIntent().getDoubleExtra("total", 0);
        ArrayList<String> services = getIntent().getStringArrayListExtra("services");

        // Set data to the TextViews
        nameSurnameTextView.setText("Name: " + name + " " + surname);
        dateTimeTextView.setText("Appointment: " + dateTime);
        servicesTextView.setText("Booked Services: " + services.toString());
        totalTextView.setText("Total: R " + total);

        // Set up the back to home button
        backToHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(ConfirmationActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }
}
