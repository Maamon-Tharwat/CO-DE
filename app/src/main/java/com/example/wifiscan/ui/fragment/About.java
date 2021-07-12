package com.example.wifiscan.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wifiscan.R;
import com.example.wifiscan.databinding.FragmentAboutBinding;
import com.example.wifiscan.model.DeviceModel;
import com.example.wifiscan.model.NotificationModel;
import com.example.wifiscan.model.UserModel;
import com.example.wifiscan.ui.activities.SignInActivity;
import com.example.wifiscan.ui.viewModels.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class About extends Fragment {

    public static final String USERS = "users";
    public static final String NOTIFICATION = "notification";
    public static final String NEARBY = "nearby";
    private static final String TAG = "About";
    private UserModel model;


    public About() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentAboutBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false);

        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.firebaseUserMutableLiveData.observe(getViewLifecycleOwner(), s -> {
            if (s.equals("Sign out")) {
                Intent intent = new Intent(getContext(), SignInActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        userViewModel.getCurrentUser();
        userViewModel.userData.observe(getViewLifecycleOwner(), userModel -> {
            if (userModel != null) {
                binding.profileJob.setText(userModel.getJob());
                binding.profileNatID.setText(userModel.getNationalID());
                binding.infected.setSelected(userModel.isInfected());
                model = userModel;
            }
        });

        binding.profileSignout.setOnClickListener(v -> userViewModel.signOut());

        binding.infect.setOnSelectListener(themedButton -> {
            if (themedButton.isSelected()) {
                sendNotification();
            }
            return null;
        });
        if (binding.infected.isSelected()) {
            sendNotification();
        }


        return binding.getRoot();
    }

    public void sendNotification() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database.collection(USERS).document(currentUserID).set(model);
        database.collection(USERS).document(currentUserID).collection(NEARBY).addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w(TAG, "listen:error", error);
                return;
            }
            assert value != null;
            for (DocumentChange dc : value.getDocumentChanges()) {
                if (dc.getType() == DocumentChange.Type.ADDED) {
                    DeviceModel model = dc.getDocument().toObject(DeviceModel.class);
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MMMM-dd  hh:mm aaa", Locale.getDefault());
                    try {
                        Date date = df.parse(model.getDate());
                        if (getDifference(date, Calendar.getInstance().getTime()) <= 14) {
                            database.collection(USERS).document(currentUserID)
                                    .collection(NOTIFICATION).document(model.getId()).set(new NotificationModel(model.getDate()));
                        }
                        database.collection(USERS).document(currentUserID).collection(NEARBY).document(dc.getDocument().getId()).delete();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

    }

    public long getDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;

        if (elapsedHours >= 12) {
            elapsedDays++;
        }
        return elapsedDays;
    }

}