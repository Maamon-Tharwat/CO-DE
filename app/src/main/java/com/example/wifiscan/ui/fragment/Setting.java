package com.example.wifiscan.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wifiscan.R;
import com.example.wifiscan.databinding.FragmentSettingBinding;
import com.example.wifiscan.model.UserModel;
import com.example.wifiscan.ui.viewModels.UserViewModel;

public class Setting extends Fragment {

    private UserModel model;

    public Setting() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentSettingBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);

        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getCurrentUser();
        userViewModel.userData.observe(getViewLifecycleOwner(), userModel -> {
            if (userModel != null) {

                binding.profileName.setText(userModel.getName());
                binding.profileAddress.setText(userModel.getAddress());
                binding.profileEmail.setText(userModel.getEmail());
                binding.profilePhone.setText(userModel.getPhone());

                model = userModel;

            }
        });

        binding.editEmail.setOnClickListener(v -> {
            if (binding.profileEmail.isEnabled()) {
                binding.editEmail.setText("Edit");
                binding.profileEmail.setEnabled(false);
                model.setEmail(binding.profileEmail.getText().toString());
                userViewModel.addUser(model);

            } else {
                binding.editEmail.setText("Done");
                binding.profileEmail.setEnabled(true);
            }
        });

        binding.editName.setOnClickListener(v -> {
            if (binding.profileName.isEnabled()) {
                binding.editName.setText("Edit");
                binding.profileName.setEnabled(false);
                model.setName(binding.profileName.getText().toString());
                userViewModel.addUser(model);
            } else {
                binding.editName.setText("Done");
                binding.profileName.setEnabled(true);
            }
        });
        binding.editAddress.setOnClickListener(v -> {
            if (binding.profileAddress.isEnabled()) {
                binding.editAddress.setText("Edit");
                binding.profileAddress.setEnabled(false);
                model.setAddress(binding.profileAddress.getText().toString());
                userViewModel.addUser(model);
            } else {
                binding.editAddress.setText("Done");
                binding.profileAddress.setEnabled(true);
            }
        });
        binding.editPhone.setOnClickListener(v -> {
            if (binding.profilePhone.isEnabled()) {
                binding.editPhone.setText("Edit");
                binding.profilePhone.setEnabled(false);
                model.setPhone(binding.profilePhone.getText().toString());
                userViewModel.addUser(model);
            } else {
                binding.editPhone.setText("Done");
                binding.profilePhone.setEnabled(true);
            }
        });


        return binding.getRoot();
    }


}

