package com.example.wifiscan.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.wifiscan.R;
import com.example.wifiscan.databinding.FragmentProfileBinding;
import com.example.wifiscan.ui.activities.SignInActivity;
import com.example.wifiscan.ui.activities.UserViewModel;
import com.google.android.material.tabs.TabLayout;

public class Profile extends Fragment {


    public Profile() {
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
        FragmentProfileBinding binding= DataBindingUtil.inflate(inflater,R.layout.fragment_profile, container, false);
        View root=binding.getRoot();
        UserViewModel userViewModel=new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.firebaseUserMutableLiveData.observe(this,s -> {
            if(s.equals("Sign out")){
                Intent intent=new Intent(getContext(), SignInActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        userViewModel.getCurrentUser();
        userViewModel.userData.observe(this,userModel -> {
            if(userModel != null) {
                binding.profileUserName.setText(userModel.getName());
                binding.profileName.setText(userModel.getName());
                binding.profileAddress.setText(userModel.getAddress());
                binding.profileEmail.setText(userModel.getEmail());
                binding.profileJob.setText(userModel.getJob());
                binding.profileNatID.setText(userModel.getNationalID());
                binding.profilePhone.setText(userModel.getPhone());
            }
        });
        binding.profileSignout.setOnClickListener(v -> userViewModel.signOut());

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    binding.profileabout.setVisibility(View.VISIBLE);
                    binding.profilesettings.setVisibility(View.GONE);
                }else if (tab.getPosition()==1){
                    binding.profileabout.setVisibility(View.GONE);
                    binding.profilesettings.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return root;
    }
}