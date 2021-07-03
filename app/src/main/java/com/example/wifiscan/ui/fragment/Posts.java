package com.example.wifiscan.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wifiscan.R;
import com.example.wifiscan.adapter.PostAdapter;
import com.example.wifiscan.databinding.FragmentPostsBinding;
import com.example.wifiscan.model.PostModel;
import com.example.wifiscan.model.UserModel;
import com.example.wifiscan.ui.activities.NewPostActivity;
import com.example.wifiscan.ui.activities.UserViewModel;

import java.util.ArrayList;

public class Posts extends Fragment {


    public Posts() {
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
        FragmentPostsBinding binding= DataBindingUtil.inflate(inflater,R.layout.fragment_posts, container, false);
        View root=binding.getRoot();
        PostViewModel postViewModel=new ViewModelProvider(this).get(PostViewModel.class);
        UserViewModel userViewModel=new ViewModelProvider(this).get(UserViewModel.class);

        PostAdapter adapter=new PostAdapter(userViewModel.userData.getValue());
        binding.postsrecycler.setAdapter(adapter);
        binding.postsrecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.postsadd.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), NewPostActivity.class);
            startActivity(intent);
        });

        postViewModel.getPosts();
        postViewModel.posts.observe(this,postModels -> {
            adapter.setPosts(postModels);
            adapter.notifyDataSetChanged();
        });
        return root;
    }
}