package com.example.wifiscan.ui.viewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wifiscan.model.DonationModel;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class DonationViewModel extends ViewModel {

    public static final String COLLECTION_PATH = "donations";
    private static final String TAG = "DonationViewModel";
    private final FirebaseFirestore database;
    public MutableLiveData<ArrayList<DonationModel>> donations;

    public DonationViewModel() {
        database = FirebaseFirestore.getInstance();
        donations = new MutableLiveData<>();

    }

    public void getDonations() {
        donations.setValue(new ArrayList<>());

        database.collection(COLLECTION_PATH).addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w(TAG, "listen:error", error);
                return;
            }
            assert value != null;
            for (DocumentChange dc : value.getDocumentChanges()) {
                if (dc.getType() == DocumentChange.Type.ADDED) {
                    Objects.requireNonNull(donations.getValue()).add(dc.getDocument().toObject(DonationModel.class));
                    donations.setValue(donations.getValue());
                }
            }

        });
    }

    public void addDonation(DonationModel donation) {
        database.collection(COLLECTION_PATH)
                .document(donation.getId())
                .set(donation);
    }
}
