package com.example.wifiscan.services;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class CODEService {

    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    private static final String TAG = "CODEService";
    private static final String appname = "CO-DE";
    private static final UUID uuid = UUID.fromString("990de5c8-2783-44c9-9d20-0c7bb75a1e36");
    private final BluetoothAdapter bluetoothAdapter;
    private final Handler handler;
    Context context;
    private String message;
    private AcceptThread acceptThread;
    private BluetoothDevice device;
    private UUID deviceUUID;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;


    public CODEService(Context context, Handler handler1) {
        this.context = context;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        start();
        handler = handler1;
    }

    //start accept thread
    public synchronized void start() {
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        if (acceptThread == null) {
            acceptThread = new AcceptThread();
            acceptThread.start();
        }
    }

    public void startClient(BluetoothDevice device1, UUID uuid, String message) {
        this.message = message;
        connectThread = new ConnectThread(device1, uuid);
        connectThread.start();

    }

    private void connected(BluetoothSocket socket, BluetoothDevice device) {
        connectedThread = new ConnectedThread(socket);
        connectedThread.start();
        write(message.getBytes());
    }

    public void write(byte[] bytes) {
//        ConnectedThread thread;

        connectedThread.write(bytes);
    }

    //server class
    private class AcceptThread extends Thread {
        private final BluetoothServerSocket serverSocket;

        public AcceptThread() {
            BluetoothServerSocket socket = null;

            try {
                socket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appname, uuid);
            } catch (IOException e) {
                e.printStackTrace();
            }
            serverSocket = socket;
        }

        @Override
        public void run() {
            BluetoothSocket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (socket != null) {
                connected(socket, device);
            }
        }

        public void cancel() {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Connection class
    private class ConnectThread extends Thread {
        private BluetoothSocket socket;

        public ConnectThread(BluetoothDevice device1, UUID uuid) {
            device = device1;
            deviceUUID = uuid;
        }

        @Override
        public void run() {
            BluetoothSocket temp = null;

            try {
                temp = device.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket = temp;

            try {
                socket.connect();
            } catch (IOException e) {
                e.printStackTrace();

                try {
                    socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

            connected(socket, device);
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket socket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public ConnectedThread(BluetoothSocket socket1) {
            this.socket = socket1;

            InputStream tempin = null;
            OutputStream tempout = null;


            try {
                tempin = socket.getInputStream();
                tempout = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputStream = tempin;
            outputStream = tempout;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            while (true) {

                try {
                    bytes = inputStream.read(buffer);
                    handler.obtainMessage(MESSAGE_READ, bytes, 1, buffer)
                            .sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        public void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());

            try {
                outputStream.write(bytes);
                handler.obtainMessage(MESSAGE_WRITE, text.length(), 1, bytes)
                        .sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
            }
            cancel();
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
