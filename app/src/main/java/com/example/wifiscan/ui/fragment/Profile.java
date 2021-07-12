package com.example.wifiscan.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.wifiscan.R;
import com.example.wifiscan.adapter.TabsAdapter;
import com.example.wifiscan.databinding.FragmentProfileBinding;
import com.example.wifiscan.model.UserModel;
import com.example.wifiscan.ui.viewModels.UserViewModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class Profile extends Fragment {
    private static final String TAG = "Profile";

    UserModel model;
    private UserViewModel userViewModel;
    private FragmentProfileBinding binding;


    public Profile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getCurrentUser();
        userViewModel.userData.observe(getViewLifecycleOwner(), userModel -> {
            if (userModel != null) {
                binding.profileUserName.setText(userModel.getName());

                if (userModel.getImage() != null) {
                    int radius = 125; // corner radius, higher value = more rounded
                    int margin = 2; // crop margin, set to 0 for corners with no crop
                    Glide.with(getContext())
                            .load(userModel.getImage())
                            .centerCrop() // scale image to fill the entire ImageView
                            .transform(new RoundedCornersTransformation(radius, margin))
                            .into(binding.profilePic);
                }
                model = userModel;
            }
        });

        binding.profileImageChoose.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new About());
        fragments.add(new Setting());


        TabsAdapter adapter = new TabsAdapter(getActivity().getSupportFragmentManager(), fragments);
        binding.pager.setAdapter(adapter);
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return binding.getRoot();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                    model.setImage(downloadUri.toString());
                    userViewModel.addUser(model);
                    int radius = 125; // corner radius, higher value = more rounded
                    int margin = 2; // crop margin, set to 0 for corners with no crop
                    Glide.with(getContext())
                            .load(model.getImage())
                            .centerCrop() // scale image to fill the entire ImageView
                            .transform(new RoundedCornersTransformation(radius, margin))
                            .into(binding.profilePic);
                } else {
                    // Handle failures
                    // ...
                }
            });
        }
    }
}