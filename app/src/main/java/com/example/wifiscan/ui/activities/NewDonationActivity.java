package com.example.wifiscan.ui.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.wifiscan.R;
import com.example.wifiscan.model.DonationModel;
import com.example.wifiscan.ui.viewModels.DonationViewModel;
import com.example.wifiscan.ui.viewModels.UserViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewDonationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_donation);

        DonationModel donation = new DonationModel();
        UserViewModel viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.getCurrentUser();
        viewModel.userData.observe(this, model -> {
            donation.setUserID(model.getId());
            donation.setUserName(model.getName());
            donation.setUserImage(model.getImage());
            donation.setUserPhone(model.getPhone());

        });
        DonationViewModel donationViewModel = new ViewModelProvider(this).get(DonationViewModel.class);

        EditText medName = findViewById(R.id.addDonationMedName);
        EditText des = findViewById(R.id.addDonationDescription);
        EditText price = findViewById(R.id.addDonationPrice);
        EditText amount = findViewById(R.id.addDonationAmount);
        Button add = findViewById(R.id.addDonation);

        add.setOnClickListener(v -> {
            Date c = Calendar.getInstance().getTime();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MMMM-dd  hh:mm aaa", Locale.getDefault());
            String formattedDate = df.format(c);
            donation.setDate(formattedDate);
            donation.setId(donation.getUserID() + donation.getDate());
            donation.setMedName(medName.getText().toString());
            donation.setDescription(des.getText().toString());
            donation.setPrice(Double.parseDouble(price.getText().toString()));
            donation.setAmount(Integer.parseInt(amount.getText().toString()));

            donationViewModel.addDonation(donation);
            finish();
        });
    }
}