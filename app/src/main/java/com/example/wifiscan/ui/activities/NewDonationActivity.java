package com.example.wifiscan.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.wifiscan.R;
import com.example.wifiscan.model.DonationModel;
import com.example.wifiscan.ui.viewModels.DonationViewModel;
import com.example.wifiscan.ui.viewModels.UserViewModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewDonationActivity extends AppCompatActivity {

    private static final String TAG = "NewDonationActivity";

    private DonationModel donation;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_donation);

        donation = new DonationModel();
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
        image = findViewById(R.id.addDonationImage);

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

        image.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onActivityResult: ");
            Uri uri = data.getData();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child("images/" + uri.getLastPathSegment());
            imageRef.putFile(uri)
                    // Register observers to listen for when the download is done or if it fails
                    .addOnFailureListener(exception -> {
                        // Handle unsuccessful uploads
                    }).addOnSuccessListener(taskSnapshot -> {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            })
                    .continueWithTask(task -> {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return imageRef.getDownloadUrl();
                    }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    donation.setImage(downloadUri.toString());

                    int radius = 125; // corner radius, higher value = more rounded
                    int margin = 2; // crop margin, set to 0 for corners with no crop
                    Glide.with(this)
                            .load(donation.getImage())
                            .centerCrop() // scale image to fill the entire ImageView
                            .into(image);

                } else {
                    // Handle failures
                    // ...
                }
            });


        }
    }
}