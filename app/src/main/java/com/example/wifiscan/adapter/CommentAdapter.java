package com.example.wifiscan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.wifiscan.R;
import com.example.wifiscan.model.CommentModel;
import com.example.wifiscan.model.PostModel;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER=1;
    private static final int COMMENT=2;

    PostModel postModel;
    ArrayList<CommentModel> data;

    public CommentAdapter(PostModel postModel) {
        this.postModel = postModel;
        this.data = new ArrayList<>();
    }

    public ArrayList<CommentModel> getData() {
        return data;
    }

    public void setData(ArrayList<CommentModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        if(viewType==HEADER){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commentheader, parent, false);
            return new CommentAdapter.HeaderHolder(view);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commentitem, parent, false);
            return new CommentHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderHolder){
            HeaderHolder headerHolder= (HeaderHolder) holder;
            headerHolder.username.setText(postModel.getUserName());
            headerHolder.postDate.setText(postModel.getDate());
            headerHolder.post.setText(postModel.getDescription());
        }else if(holder instanceof CommentHolder){
            CommentHolder commentHolder= (CommentHolder) holder;
            commentHolder.username.setText(data.get(position-1).getUserName());
            commentHolder.comment.setText(data.get(position-1).getComment());
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
    class HeaderHolder extends RecyclerView.ViewHolder{

        ImageView postImages;
        ImageView userImage;
        TextView username;
        TextView postDate;
        TextView post;

        public HeaderHolder(@NonNull  View itemView) {
            super(itemView);
            postImages=itemView.findViewById(R.id.headerImages);
            userImage=itemView.findViewById(R.id.headerUserImage);
            username=itemView.findViewById(R.id.headerUser);
            postDate=itemView.findViewById(R.id.headerDate);
            post=itemView.findViewById(R.id.headerPost);

        }
    }

    class CommentHolder extends RecyclerView.ViewHolder {

        ImageView userImage;
        TextView username;
        TextView comment;
        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            userImage=itemView.findViewById(R.id.commentUserImage);
            username=itemView.findViewById(R.id.commentUser);
            comment=itemView.findViewById(R.id.comment);
        }
    }
}
