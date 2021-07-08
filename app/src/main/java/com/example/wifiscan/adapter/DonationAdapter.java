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

import com.example.wifiscan.R;
import com.example.wifiscan.model.DonationModel;
import com.example.wifiscan.ui.activities.DonationDetailsActivity;

import java.util.ArrayList;

public class DonationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public static final int HEADER = 1;
    public static final int DONATION = 2;
    ArrayList<DonationModel> data;

    Context context;

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
                Intent intent = new Intent(context, DonationDetailsActivity.class);
                intent.putExtra("donation", data.get(position));
                context.startActivity(intent);
            });
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

    class DonationHolder extends RecyclerView.ViewHolder {

        ImageView medImage;
        TextView medName;
        CardView view;

        public DonationHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            view = itemView.findViewById(R.id.donationItemCard);
            medImage = itemView.findViewById(R.id.donationItemMedImage);
            medName = itemView.findViewById(R.id.donationItemMedName);
        }
    }
}
