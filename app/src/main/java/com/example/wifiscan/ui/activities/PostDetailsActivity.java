package com.example.wifiscan.ui.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wifiscan.R;
import com.example.wifiscan.adapter.CommentAdapter;
import com.example.wifiscan.model.CommentModel;
import com.example.wifiscan.model.PostModel;
import com.example.wifiscan.ui.viewModels.CommentViewModel;
import com.example.wifiscan.ui.viewModels.UserViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PostDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        CommentModel commentModel = new CommentModel();

        CommentViewModel commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);

        String postID = ((PostModel) getIntent().getSerializableExtra("post")).getId();
        commentModel.setPostID(postID);
        commentViewModel.getComments(postID);
        CommentAdapter adapter=new CommentAdapter((PostModel) getIntent().getSerializableExtra("post"));
        RecyclerView recyclerView=findViewById(R.id.commentRecycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentViewModel.comments.observe(this,commentModels -> {
            adapter.setData(commentModels);
            adapter.notifyDataSetChanged();
        });

        UserViewModel userViewModel=new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getCurrentUser();
        userViewModel.userData.observe(this,userModel -> {
            commentModel.setUserID(userModel.getId());
            commentModel.setUserName(userModel.getName());
            commentModel.setUserImage(userModel.getImage());

        });


        EditText etComment=findViewById(R.id.detailsComment);
        Button send=findViewById(R.id.sendComment);
        send.setOnClickListener(v -> {
            Date c = Calendar.getInstance().getTime();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MMMM-dd  hh:mm aaa", Locale.getDefault());
            String formattedDate = df.format(c);
            commentModel.setDate(formattedDate);
            commentModel.setId(commentModel.getUserID()+formattedDate);
            commentModel.setComment(etComment.getText().toString());

            commentViewModel.addComment(postID,commentModel);
            etComment.setText("");

        });
    }
}