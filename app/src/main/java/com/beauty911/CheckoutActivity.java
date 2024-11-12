package com.beauty911;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private double totalCost;
    private EditText nameInput, surnameInput, dateTimeInput;
    private ArrayList<String> bookedServices;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        db = FirebaseFirestore.getInstance();

        // Initialize views
        nameInput = findViewById(R.id.nameInput);
        surnameInput = findViewById(R.id.surnameInput);
        dateTimeInput = findViewById(R.id.selectedDateTime);

        // Assume totalCost and bookedServices are passed from CartActivity
        totalCost = getIntent().getDoubleExtra("totalCost", 0);
        bookedServices = getIntent().getStringArrayListExtra("bookedServices");

        // Assume you have a button for proceeding with payment
        findViewById(R.id.makePaymentButton).setOnClickListener(v -> initiatePayment());
    }

    private void initiatePayment() {
        // Call your payment processing API or logic here
        // For simplicity, we will assume the payment is successful

        // Handle payment result
        handlePaymentResult(new PaymentResult(true)); // Assuming payment was successful
    }

    private void handlePaymentResult(PaymentResult result) {
        if (result.isSuccess()) {
            // Retrieve user details and booked services
            String name = nameInput.getText().toString();
            String surname = surnameInput.getText().toString();
            String dateTime = dateTimeInput.getText().toString();
            double total = totalCost;
            ArrayList<String> bookedServices = getBookedServices();  // Get services from CartActivity or SharedPreferences

            // Save booking details to Firestore
            saveBookingToFirestore(name, surname, dateTime, bookedServices, total);

            // Pass data to ConfirmationActivity
            Intent intent = new Intent(this, ConfirmationActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("surname", surname);
            intent.putExtra("dateTime", dateTime);
            intent.putExtra("total", total);
            intent.putStringArrayListExtra("services", bookedServices);
            startActivity(intent);

        } else {
            // Handle failed payment
            Toast.makeText(this, "Payment failed: " + result.getError(), Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<String> getBookedServices() {
        // Retrieve booked services from wherever you are storing them (e.g., SharedPreferences, CartActivity, etc.)
        // For simplicity, here we return the list passed in the intent
        return bookedServices;
    }

    private void saveBookingToFirestore(String name, String surname, String dateTime, ArrayList<String> bookedServices, double total) {
        // Create a new document with booking data
        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("name", name + " " + surname);
        bookingData.put("dateTime", dateTime);
        bookingData.put("bookedServices", bookedServices);
        bookingData.put("totalAmount", total);

        // Save the data in the "Checkout" collection
        db.collection("Checkout")
                .add(bookingData)
                .addOnSuccessListener(documentReference -> {
                    // Successfully added data to Firestore
                    Log.d("Firestore", "Booking details saved successfully.");
                })
                .addOnFailureListener(e -> {
                    // Handle failure to save data
                    Log.w("Firestore", "Error adding document", e);
                });
    }
}
