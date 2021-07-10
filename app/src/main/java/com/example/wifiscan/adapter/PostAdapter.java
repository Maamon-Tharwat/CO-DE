package com.example.wifiscan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wifiscan.R;
import com.example.wifiscan.model.PostModel;
import com.example.wifiscan.ui.activities.PostDetailsActivity;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    ArrayList<PostModel> posts;
    Context context;

    public PostAdapter() {
        this.posts = new ArrayList<>();
    }

    public void setPosts(ArrayList<PostModel> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.user.setText(posts.get(position).getUserName());
        holder.date.setText(posts.get(position).getDate());
        holder.title.setText(posts.get(position).getTitle());
        holder.post.setText(posts.get(position).getDescription());
        holder.more.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostDetailsActivity.class);
            intent.putExtra("post", posts.get(position));
            context.startActivity(intent);
        });
        if (posts.get(position).getUserImage() != null) {
            int radius = 125; // corner radius, higher value = more rounded
            int margin = 2; // crop margin, set to 0 for corners with no crop
            Glide.with(context)
                    .load(posts.get(position).getUserImage())
                    .centerCrop() // scale image to fill the entire ImageView
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView user;
        TextView date;
        TextView title;
        TextView post;
        Button more;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context=itemView.getContext();
            image=itemView.findViewById(R.id.headerUserImage);
            user=itemView.findViewById(R.id.headerUser);
            date=itemView.findViewById(R.id.headerDate);
            title=itemView.findViewById(R.id.postitemtitle);
            post=itemView.findViewById(R.id.postitempost);
            more=itemView.findViewById(R.id.itempostcomment);
        }
    }
}
