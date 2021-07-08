package com.example.wifiscan.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wifiscan.R;
import com.example.wifiscan.adapter.DonationAdapter;
import com.example.wifiscan.ui.activities.NewDonationActivity;
import com.example.wifiscan.ui.viewModels.DonationViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class Donations extends Fragment {

    public Donations() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_donations, container, false);

        DonationViewModel donationViewModel = new ViewModelProvider(this).get(DonationViewModel.class);

        DonationAdapter adapter = new DonationAdapter();
        RecyclerView recycler = root.findViewById(R.id.donationRecycler);
        recycler.setAdapter(adapter);

        FloatingActionButton add = root.findViewById(R.id.donationNewDonation);
        add.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), NewDonationActivity.class);
            startActivity(intent);
        });

        donationViewModel.getDonations();
        donationViewModel.donations.observe(this, donationModels -> {
            adapter.setData(donationModels);
            adapter.notifyDataSetChanged();
        });


        return root;
    }
}