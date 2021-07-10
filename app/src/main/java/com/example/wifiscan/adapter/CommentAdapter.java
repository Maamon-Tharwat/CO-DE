package com.example.wifiscan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wifiscan.R;
import com.example.wifiscan.model.CommentModel;
import com.example.wifiscan.model.PostModel;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER = 1;
    private static final int COMMENT = 2;
    int radius = 125; // corner radius, higher value = more rounded
    int margin = 2; // crop margin, set to 0 for corners with no crop


    PostModel postModel;
    ArrayList<CommentModel> data;


    public CommentAdapter(PostModel postModel) {
        this.postModel = postModel;
        this.data = new ArrayList<>();
    }

    public void setData(ArrayList<CommentModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        if(viewType==HEADER){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_header, parent, false);
            return new HeaderHolder(view);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
            return new CommentHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderHolder) {
            HeaderHolder headerHolder = (HeaderHolder) holder;
            headerHolder.username.setText(postModel.getUserName());
            headerHolder.postDate.setText(postModel.getDate());
            headerHolder.post.setText(postModel.getDescription());
            if (postModel.getUserImage() != null) {
                Glide.with(headerHolder.context)
                        .load(postModel.getUserImage())
                        .centerCrop() // scale image to fill the entire ImageView
                        .transform(new RoundedCornersTransformation(radius, margin))
                        .into(headerHolder.userImage);
            }
            if (postModel.getImageUrl() != null) {
                Glide.with(headerHolder.context)
                        .load(postModel.getImageUrl())
                        .centerCrop() // scale image to fill the entire ImageView
                        .into(headerHolder.postImages);

            }
        }else if(holder instanceof CommentHolder) {
            CommentHolder commentHolder = (CommentHolder) holder;
            commentHolder.username.setText(data.get(position - 1).getUserName());
            commentHolder.comment.setText(data.get(position - 1).getComment());
            if (data.get(position - 1).getUserImage() != null) {
                Glide.with(commentHolder.context)
                        .load(data.get(position - 1).getUserImage())
                        .centerCrop() // scale image to fill the entire ImageView
                        .transform(new RoundedCornersTransformation(radius, margin))
                        .into(commentHolder.userImage);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return HEADER;
        }else {
            return COMMENT;
        }
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {

        ImageView postImages;
        ImageView userImage;
        TextView username;
        TextView postDate;
        TextView post;
        Context context;

        public HeaderHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            postImages = itemView.findViewById(R.id.headerImages);
            userImage=itemView.findViewById(R.id.headerUserImage);
            username=itemView.findViewById(R.id.headerUser);
            postDate=itemView.findViewById(R.id.headerDate);
            post=itemView.findViewById(R.id.headerPost);

        }
    }

    static class CommentHolder extends RecyclerView.ViewHolder {

        Context context;
        ImageView userImage;
        TextView username;
        TextView comment;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            userImage = itemView.findViewById(R.id.commentUserImage);
            username = itemView.findViewById(R.id.commentUser);
            comment=itemView.findViewById(R.id.comment);
        }
    }
}
