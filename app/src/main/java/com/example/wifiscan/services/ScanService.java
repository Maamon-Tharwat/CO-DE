package com.example.wifiscan.services;

import android.Manifest;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.wifiscan.model.DeviceModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class ScanService extends Service {
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    private static final String TAG = "ScanService";
    private static final UUID uuid = UUID.fromString("990de5c8-2783-44c9-9d20-0c7bb75a1e36");
    CODEService CODEService;
    TimerTask schedule;
    private String message;
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;
    private boolean state = false;
    private BluetoothAdapter bluetoothAdapter;


    public ScanService() {
        super();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        message = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        HandlerThread thread = new HandlerThread("ScanService", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);

        Intent intent = new Intent(ScanService.this, UploadService.class);
        startService(intent);
        state = true;

        schedule = new TimerTask() {
            @Override
            public void run() {
                loop();
            }
        };
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Scan Started", Toast.LENGTH_SHORT).show();
        Timer timer = new Timer();
        timer.schedule(schedule, 0, 12000);

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Scan Finished", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ScanService.this, UploadService.class);
        stopService(intent);
        state = false;
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }


    public void startConnection(BluetoothDevice device, UUID uuid) {
        CODEService.startClient(device, uuid, message);
    }


    private void loop() {
        if (state) {
            if (!bluetoothAdapter.isDiscovering())
                bluetooth();
        }
    }

    private void bluetooth() {

        if (bluetoothAdapter.startDiscovery()) {
            // Toast.makeText(getApplicationContext(), "scan", Toast.LENGTH_LONG).show();
            Log.d(TAG, "bluetooth: scan");
            //Finding devices
            // Get the BluetoothDevice object from the Intent
            // Add the name and address to an array adapter to show in a ListView
            BroadcastReceiver mReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();

                    //Finding devices
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        //Toast.makeText(getApplicationContext(), "found", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onReceive: found");
                        // Get the BluetoothDevice object from the Intent
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        // Add the name and address to an array adapter to show in a ListView
                        CODEService = new CODEService(getApplicationContext(), serviceHandler);
                        startConnection(device, uuid);


                    }
                }
            };

            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, filter);
            if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                Intent dIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                dIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
                startActivity(dIntent);
            }

        } else {
            //Toast.makeText(getApplicationContext(), "no", Toast.LENGTH_LONG).show();
            Log.d(TAG, "bluetooth: no");
        }

    }


    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_READ:
                    Date c = Calendar.getInstance().getTime();

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MMMM-dd  hh:mm aaa", Locale.getDefault());
                    String formattedDate = df.format(c);
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, location -> {
                        DeviceModel model = new DeviceModel(new String((byte[]) msg.obj, 0, msg.arg1), formattedDate, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                        if (UploadService.nearby == null) {
                            UploadService.nearby = new ArrayList<>();
                        }
                        UploadService.nearby.add(model);
                    });

                    break;
                case MESSAGE_WRITE:
                    break;
            }

        }
    }


}
