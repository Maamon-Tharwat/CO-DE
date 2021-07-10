package com.example.wifiscan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wifiscan.R;
import com.example.wifiscan.model.DonationModel;
import com.example.wifiscan.ui.activities.DonationDetailsActivity;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DonationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public static final int HEADER = 1;
    public static final int DONATION = 2;
    ArrayList<DonationModel> data;


    public DonationAdapter() {
        this.data = new ArrayList<>();
    }

    public void setData(ArrayList<DonationModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donation_header, parent, false);
            return new HeaderHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donation_item, parent, false);
            return new DonationHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DonationHolder) {
            DonationHolder donationHolder = (DonationHolder) holder;
            donationHolder.medName.setText(data.get(position).getMedName());
            donationHolder.view.setOnClickListener(v -> {
                Intent intent = new Intent(donationHolder.context, DonationDetailsActivity.class);
                intent.putExtra("donation", data.get(position));
                donationHolder.context.startActivity(intent);
            });
            if (data.get(position).getImage() != null) {
                int radius = 125; // corner radius, higher value = more rounded
                int margin = 2; // crop margin, set to 0 for corners with no crop
                Glide.with(donationHolder.context)
                        .load(data.get(position).getImage())
                        .centerCrop() // scale image to fill the entire ImageView
                        .transform(new RoundedCornersTransformation(radius, margin))
                        .into(donationHolder.medImage);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
//        if(position==0){
//            return HEADER;
//        }else {
//            return DONATION;
//        }
        return DONATION;
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {

        public HeaderHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class DonationHolder extends RecyclerView.ViewHolder {

        ImageView medImage;
        TextView medName;
        CardView view;
        Context context;

        public DonationHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            view = itemView.findViewById(R.id.donationItemCard);
            medImage = itemView.findViewById(R.id.donationItemMedImage);
            medName = itemView.findViewById(R.id.donationItemMedName);
        }
    }
}
