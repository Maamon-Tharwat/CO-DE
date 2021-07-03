package com.example.wifiscan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.wifiscan.R;
import com.example.wifiscan.model.DonationModel;

import java.util.ArrayList;

public class DonationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public static final int HEADER = 1;
    public static final int DONATION = 2;
    ArrayList<DonationModel> data=new ArrayList<>();

    public DonationAdapter() {
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        if(viewType== HEADER){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donationheader, parent, false);
            return new HeaderHolder(view);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_donations, parent, false);
            return new DonationHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderHolder){

        }else if(holder instanceof DonationHolder){

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
            return DONATION;
        }
    }

    class DonationHolder extends RecyclerView.ViewHolder {

        public DonationHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
    class HeaderHolder extends RecyclerView.ViewHolder{

        public HeaderHolder(@NonNull  View itemView) {
            super(itemView);
        }
    }
}
