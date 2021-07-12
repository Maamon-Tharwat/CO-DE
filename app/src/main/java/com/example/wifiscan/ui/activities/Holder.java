package com.example.wifiscan.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.wifiscan.R;
import com.example.wifiscan.adapter.TabsAdapter;
import com.example.wifiscan.ui.fragment.Donations;
import com.example.wifiscan.ui.fragment.Notification;
import com.example.wifiscan.ui.fragment.Posts;
import com.example.wifiscan.ui.fragment.Profile;
import com.gauravk.bubblenavigation.BubbleNavigationConstraintView;

import java.util.ArrayList;


public class Holder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holder);

        ViewPager pager = findViewById(R.id.holderviewpager);
        BubbleNavigationConstraintView tabs = findViewById(R.id.holdertabs);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new Posts());
        fragments.add(new Donations());
        fragments.add(new Profile());
        fragments.add(new Notification());

        TabsAdapter adapter = new TabsAdapter(getSupportFragmentManager(), fragments);
        tabs.setNavigationChangeListener((view, position) -> {
            //navigation changed, do something
            pager.setCurrentItem(position);
        });


        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tabs.setCurrentActiveItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
}