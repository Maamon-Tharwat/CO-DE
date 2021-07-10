package com.example.wifiscan.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.wifiscan.model.DeviceModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class UploadService extends Service {

    private static final String TAG = "UploadService";

    public static ArrayList<DeviceModel> nearby;
    TimerTask task;
    private boolean state = false;
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    public UploadService() {
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
        HandlerThread thread = new HandlerThread("UploadService", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);

        nearby = new ArrayList<>();
        state = true;
        task = new TimerTask() {
            @Override
            public void run() {
                isInternetAvailable();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 12 * 1000);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "Upload Started", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onStartCommand: Upload Started");

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        state = false;
//        Toast.makeText(this, "Upload Finished", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onDestroy: Upload Finished");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    private void uploadNearby() {
        Log.d(TAG, "uploadNearby: ");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        for (DeviceModel model : nearby) {
            database.collection("users").document(user.getUid()).collection("nearby").document(model.getId()).set(model);
        }
        nearby.clear();
    }

    public void isInternetAvailable() {
        while (state) {
            try {
                InetAddress ipAddr = InetAddress.getByName("google.com");
                //You can replace it with your name
                if (!ipAddr.equals("") & nearby.size() != 0) {
                    uploadNearby();
                    Log.d(TAG, "isInternetAvailable: ");
                    break;
                }

            } catch (Exception ignored) {

            }
        }
    }

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

        }
    }
}
