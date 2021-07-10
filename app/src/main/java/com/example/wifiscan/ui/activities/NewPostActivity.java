package com.example.wifiscan.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.wifiscan.R;
import com.example.wifiscan.databinding.ActivityNewPostBinding;
import com.example.wifiscan.model.PostModel;
import com.example.wifiscan.ui.viewModels.PostViewModel;
import com.example.wifiscan.ui.viewModels.UserViewModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class NewPostActivity extends AppCompatActivity {
    private static final String TAG = "NewPostActivity";

    private PostModel post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNewPostBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_new_post);

        post = new PostModel();

        UserViewModel viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.getCurrentUser();
        viewModel.userData.observe(this, model -> {
            post.setUserID(model.getId());
            post.setUserName(model.getName());
            post.setUserImage(model.getImage());
            binding.newpostuser.setText(model.getName());
            if (model.getImage() != null) {
                int radius = 125; // corner radius, higher value = more rounded
                int margin = 2; // crop margin, set to 0 for corners with no crop
                Glide.with(this)
                        .load(model.getImage())
                        .centerCrop() // scale image to fill the entire ImageView
                        .transform(new RoundedCornersTransformation(radius, margin))
                        .into(binding.newPostUserImage);
            }

        });

        PostViewModel postViewModel=new ViewModelProvider(this).get(PostViewModel.class);
        binding.newpostadd.setOnClickListener(v -> {
            Date c = Calendar.getInstance().getTime();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MMMM-dd  hh:mm aaa", Locale.getDefault());
            String formattedDate = df.format(c);
            post.setDate(formattedDate);
            post.setId(post.getUserID() + post.getDate());
            post.setTitle(binding.newposttitle.getText().toString());
            post.setDescription(binding.newpostcontent.getText().toString());
            postViewModel.addPost(post);
            finish();
        });
        binding.newPostImagePicker.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: ");

            ImagePicker.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                    post.setImageUrl(downloadUri.toString());
                } else {
                    // Handle failures
                    // ...
                }
            });


        }
    }
}