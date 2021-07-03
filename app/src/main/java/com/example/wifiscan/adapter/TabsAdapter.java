package com.example.wifiscan.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.wifiscan.ui.fragment.Donations;
import com.example.wifiscan.ui.fragment.Posts;
import com.example.wifiscan.ui.fragment.Profile;


public class TabsAdapter extends FragmentPagerAdapter {
    public TabsAdapter(@NonNull  FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                return new Donations();
            case 2:
                return new Profile();
            default:
                return new Posts();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
