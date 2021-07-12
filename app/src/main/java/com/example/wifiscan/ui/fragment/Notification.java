package com.example.wifiscan.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wifiscan.R;
import com.example.wifiscan.adapter.NotificationAdapter;
import com.example.wifiscan.model.NotificationModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Notification extends Fragment {

    public static final String USERS = "users";
    public static final String NOTIFICATION = "notification";
    private static final String TAG = "Notification";

    public Notification() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_notification, container, false);

        ArrayList<NotificationModel> data = new ArrayList<>();
        NotificationAdapter adapter = new NotificationAdapter(data);
        RecyclerView recycler = root.findViewById(R.id.notificationRecycler);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database.collection(USERS).document(currentUserID).collection(NOTIFICATION).addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w(TAG, "listen:error", error);
                return;
            }
            assert value != null;
            for (DocumentChange dc : value.getDocumentChanges()) {
                if (dc.getType() == DocumentChange.Type.ADDED) {
                    data.add(dc.getDocument().toObject(NotificationModel.class));
                    adapter.notifyDataSetChanged();
                }
            }

        });

        return root;
    }
}