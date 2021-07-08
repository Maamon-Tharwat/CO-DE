package com.example.wifiscan.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wifiscan.R;
import com.example.wifiscan.model.DonationModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DonationDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_details);

        DonationModel donation = (DonationModel) getIntent().getSerializableExtra("donation");

        TextView userName = findViewById(R.id.donationDetailUserName);
        TextView medName = findViewById(R.id.donationDetailMedName);
        TextView amount = findViewById(R.id.donationDetailAmount);
        TextView description = findViewById(R.id.donationDetailDescription);
        TextView price = findViewById(R.id.donationDetailPrice);
        TextView phone = findViewById(R.id.donationDetailUserPhone);
        FloatingActionButton cal = findViewById(R.id.donationDetailCallUser);

        userName.setText(donation.getUserName());
        medName.setText(donation.getMedName());
        amount.setText(String.valueOf(donation.getAmount()));
        price.setText(String.valueOf(donation.getPrice()));
        description.setText(donation.getDescription());
        phone.setText(donation.getUserPhone());
        cal.setOnClickListener(v -> {
            String uri = "tel:" + donation.getUserPhone().trim();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        });
    }
}