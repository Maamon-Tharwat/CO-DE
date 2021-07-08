package com.example.wifiscan.adapter;

import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wifiscan.R;

import java.util.ArrayList;

public class MacAdapter extends RecyclerView.Adapter<MacAdapter.ViewHolder> {

    ArrayList<WifiP2pDevice> data;

    public ArrayList<WifiP2pDevice> getData() {
        return data;
    }

    public void setData(ArrayList<WifiP2pDevice> data) {
        this.data = data;
    }

    public MacAdapter(ArrayList<WifiP2pDevice> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(data.get(position).deviceName);
        holder.mac.setText(data.get(position).deviceAddress);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView mac;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.deviceitemname);
            mac=itemView.findViewById(R.id.deviceitemmac);
        }
    }
}
