package com.example.wifiscan.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wifiscan.R;
import com.example.wifiscan.ui.activities.SignInActivity;
import com.example.wifiscan.adapter.MacAdapter;
import com.example.wifiscan.utils.PermissionsUtils;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MacAdapter adapter;
    private ArrayList<WifiP2pDevice> data = new ArrayList<>();

    WifiP2pManager manager;
    Channel channel;
    BroadcastReceiver receiver;
    private IntentFilter intentFilter;
    PeerListListener myPeerListListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.macrecycler);
        adapter = new MacAdapter(data);


        Intent z = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(z);



        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TextView mac = findViewById(R.id.pmac);
        Button scan = findViewById(R.id.scan);
        TextView dmac = findViewById(R.id.dmac);

        Button test = findViewById(R.id.test);
        test.setOnClickListener(e -> {
            Intent i = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(i);
            finish();
        });

        mac.setText(getMacAddr());

        if (PermissionsUtils.checkAndRequest(this, Manifest.permission.ACCESS_FINE_LOCATION,
                PermissionsUtils.MY_PERMISSIONS_REQUEST_EXAMPLE, "Explain here why the app needs permissions", (dialog, which) -> {
                    // YOUR CANCEL CODE
                })) {
            // YOUR BASE METHOD
        }


        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);


        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        scan.setOnClickListener(v -> {
            Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(MainActivity.this, "discover successed", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(int reasonCode) {
                    Toast.makeText(MainActivity.this, reasonCode + "", Toast.LENGTH_SHORT).show();
                }
            });
        });



        WifiP2pConfig config=new WifiP2pConfig();

        String testmac="e2:d0:83:be:a4:e9";
        myPeerListListener = peers -> {
            if (!peers.getDeviceList().equals(adapter.getData())) {
                adapter.getData().clear();
                adapter.getData().addAll(peers.getDeviceList());
                adapter.notifyDataSetChanged();
                config.deviceAddress=testmac;
                config.wps.setup= WpsInfo.PBC;

                manager.connect(channel, config, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(),"connetion done",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int reason) {

                    }
                });
            } else {
                Toast.makeText(this, "No new devices", Toast.LENGTH_SHORT).show();
            }
            if (peers.getDeviceList().size() == 0) {
                Toast.makeText(this, "no devices found", Toast.LENGTH_SHORT).show();
            }
        };
    }

    //getting mac address from mobile
    private String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    // res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }

    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionsUtils.MY_PERMISSIONS_REQUEST_EXAMPLE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    // YOUR BASE METHOD
                } else {
                    // permission denied
                    boolean showRationale = false;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION);
                    }
                    if (!showRationale) {
                        // user denied flagging NEVER ASK AGAIN
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("WHY THIS PERM IS MANDATORY TO USED THIS APP")
                                .setPositiveButton(getResources().getString(android.R.string.ok), (dialog, which) -> {
                                    PermissionsUtils.startInstalledAppDetailsActivity(this);
                                    this.finish();
                                }).setCancelable(false).show();
                    } else {
                        // user denied WITHOUT never ask again
                        // YOUR CANCEL CODE
                    }
                }
                break;
        }
    }

}


class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager manager;
    private Channel channel;
    private MainActivity activity;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel,
                                       MainActivity activity) {
        super();
        this.manager = manager;
        this.channel = channel;
        this.activity = activity;

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity

            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled
                Toast.makeText(activity, "p2p enabled", Toast.LENGTH_SHORT).show();
            } else {
                // Wi-Fi P2P is not enabled
                Toast.makeText(activity, "p2p not enabled", Toast.LENGTH_SHORT).show();
            }

        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            // request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            if (manager != null) {
                Toast.makeText(activity, "request started", Toast.LENGTH_SHORT).show();
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                manager.requestPeers(channel, activity.myPeerListListener);
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
//            manager
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }
}

