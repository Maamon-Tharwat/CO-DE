package com.example.wifiscan.ui.activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.wifiscan.R;
import com.example.wifiscan.services.ScanService;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int GPS_REQUEST = 111;
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });


    @Override
    protected void onStart() {
        super.onStart();

    }

    @SuppressLint({"HardwareIds"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button scan = findViewById(R.id.scan);

        scan.setOnClickListener(v -> {
            turnGPSOn(isGPSEnable -> {

            });
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!bluetoothAdapter.isEnabled()) {
                Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(turnOn);

            }
            Intent intent = new Intent(MainActivity.this, ScanService.class);
            startService(intent);

        });

        Button stop = findViewById(R.id.stop);
        stop.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScanService.class);
            stopService(intent);
        });

        Button con = findViewById(R.id.start);

        con.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
        });
        request();

    }

    private void request() {
        requestLocation();
    }

    private void requestLocation() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    public void turnGPSOn(onGpsListener onGpsListener) {
        Context context = getApplicationContext();
        SettingsClient mSettingsClient;
        LocationSettingsRequest mLocationSettingsRequest;
        LocationManager locationManager;
        LocationRequest locationRequest;

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mSettingsClient = LocationServices.getSettingsClient(context);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(2 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        mLocationSettingsRequest = builder.build();

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (onGpsListener != null) {
                onGpsListener.gpsStatus(true);
            }
        } else {
            mSettingsClient
                    .checkLocationSettings(mLocationSettingsRequest)
                    .addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                            if (task.isSuccessful()) {
                                //  GPS is already enable, callback GPS status through listener
                                if (onGpsListener != null) {
                                    onGpsListener.gpsStatus(true);
                                }
                            } else {
                                ApiException e = (ApiException) task.getException();
                                assert e != null;
                                int statusCode = ((ApiException) e).getStatusCode();
                                switch (statusCode) {
                                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                        try {
                                            // Show the dialog by calling startResolutionForResult(), and check the
                                            // result in onActivityResult().
                                            ResolvableApiException rae = (ResolvableApiException) e;
                                            rae.startResolutionForResult(MainActivity.this, GPS_REQUEST);
                                        } catch (IntentSender.SendIntentException sie) {
                                            Log.i(TAG, "PendingIntent unable to execute request.");
                                        }
                                        break;
                                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                        String errorMessage = "Location settings are inadequate, and cannot be " +
                                                "fixed here. Fix in Settings.";
                                        Log.e(TAG, errorMessage);
                                        Toast.makeText((Activity) context, errorMessage, Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
        }
    }


    public interface onGpsListener {
        void gpsStatus(boolean isGPSEnable);
    }


}