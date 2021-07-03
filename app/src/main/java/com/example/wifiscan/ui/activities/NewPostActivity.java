package com.example.wifiscan.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.wifiscan.R;
import com.example.wifiscan.databinding.ActivityNewPostBinding;
import com.example.wifiscan.model.PostModel;
import com.example.wifiscan.model.UserModel;
import com.example.wifiscan.ui.fragment.PostViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNewPostBinding binding= DataBindingUtil.setContentView(this,R.layout.activity_new_post);

        PostModel post = new PostModel();

        UserViewModel viewModel=new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.getCurrentUser();
        viewModel.userData.observe(this,model -> {
            post.setUserID(model.getId());
            post.setUserName(model.getName());
            post.setUserImage(model.getImage());
            binding.newpostuser.setText(model.getName());

        });
        PostViewModel postViewModel=new ViewModelProvider(this).get(PostViewModel.class);
        binding.newpostadd.setOnClickListener(v -> {
            Date c = Calendar.getInstance().getTime();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MMMM-dd  hh:mm aaa", Locale.getDefault());
            String formattedDate = df.format(c);
            post.setDate(formattedDate);
            post.setId(post.getUserID()+post.getDate());
            post.setTitle(binding.newposttitle.getText().toString());
            post.setDescription(binding.newpostcontent.getText().toString());
            postViewModel.addPost(post);
            Intent intent=new Intent(NewPostActivity.this,Holder.class);
            startActivity(intent);
            finish();
        });
    }
}