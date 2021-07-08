package com.example.wifiscan.ui.viewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wifiscan.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class UserViewModel extends ViewModel {
    private static final String TAG = "UserViewModel";
    private static final String COLLECTION_PATH = "users";
    public MutableLiveData<String> firebaseUserMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<UserModel> userData = new MutableLiveData<>();
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore database;

    public UserViewModel() {
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
    }

    public void signIn(String email, String password) {
        if (!isSigned()) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        auth = FirebaseAuth.getInstance();
                        getCurrentUser();
                        firebaseUserMutableLiveData.setValue("log in successfully");
                    })
                    .addOnFailureListener(e -> firebaseUserMutableLiveData.setValue(e.getMessage()));
        }
    }

    public boolean isSigned() {
        if (user != null) {
            firebaseUserMutableLiveData.setValue("You are already signed in");
            getCurrentUser();
            return true;
        } else {
            return false;
        }

    }

    public void signOut() {
        auth.signOut();
        firebaseUserMutableLiveData.setValue("Sign out");
        userData = new MutableLiveData<>();
    }

    public void signUp(UserModel model) {
        auth=FirebaseAuth.getInstance();
        auth.signOut();
        auth.createUserWithEmailAndPassword(model.getEmail(), model.getPassword())
                .addOnSuccessListener(authResult -> {
                    model.setId(Objects.requireNonNull(authResult.getUser()).getUid());
                    model.setType("user");
                    database = FirebaseFirestore.getInstance();
                    database.collection(COLLECTION_PATH).document(model.getId()).set(model);
                    getCurrentUser();
                    firebaseUserMutableLiveData.setValue("Sign up successfully");
                })
                .addOnFailureListener(e -> firebaseUserMutableLiveData.setValue(e.getMessage()));

    }

    public void getCurrentUser() {
        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        database = FirebaseFirestore.getInstance();
        database.collection(COLLECTION_PATH).document(user.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> userData.setValue(documentSnapshot.toObject(UserModel.class)))
                .addOnFailureListener(e -> Log.w(TAG, "getCurrentUser: faild to load user"));

    }

}
