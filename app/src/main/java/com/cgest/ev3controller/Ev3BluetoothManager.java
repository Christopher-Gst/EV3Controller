package com.cgest.ev3controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.cgest.ev3controller.scenario.Scenario;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Set;
import java.util.UUID;

public final class Ev3BluetoothManager {

    private final static String TAG = "Ev3BluetoothManager";

    // Variables utilisées pour l'usage du Bluetooth.
    private static BluetoothAdapter mBluetoothAdapter;
    private static BluetoothDevice mDevice;
    private final static String MY_UUID = "00001101-0000-1000-8000-00805f9b34fb";
    private static BluetoothSocket mSocket = null;
    private static String mMessage = "Stop";
    private static PrintStream sender;

    public static void initialiserLeManager() {
        // On ititialise la connexion Bluetooth avec le robot et on créer le socket de communication.
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.e(TAG, "Initialisation du bluetooth...");
        initBluetooth();
        if (findBrick()) {
            try {
                Log.e(TAG, "Création du socket...");
                createSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void envoyerScenario(Scenario scenario) {
        try {
            // On envoie le scénario au robot. On le fait deux fois à cause de l'instabilité du comportement du robot face au bluetooth.
            sendMessage(scenario.getCode());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendMessage(scenario.getCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean findBrick() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            if (device.getName().equals("EV3")) {
                mDevice = device;
                return true;
            }
        }
        return false;
    }

    private static void initBluetooth() {
        Log.e(TAG, "Checking Bluetooth...");
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Device does not support Bluetooth");
        } else {
            Log.e(TAG, "Bluetooth supported");
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Log.e(TAG, "Bluetooth not enabled");
        } else {
            Log.e(TAG, "Bluetooth enabled");
        }
    }

    private static void sendMessage(String message) throws IOException {
        try {
            OutputStream os = mSocket.getOutputStream();
            sender = new PrintStream(os);
            Log.e("onSend", "Message = " + message);
            sender.println(message);
            sender.flush();
            Log.e("onSend", "Message sent");
            //mSocket.close();
            //Log.e("onSend", "Socket closed");
        } catch (IllegalStateException | NullPointerException e) {
            e.printStackTrace();
        }

    }

    private static void createSocket() throws IOException {
        try {
            UUID uuid = UUID.fromString(MY_UUID);
            mSocket = mDevice.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e("createSocket", "Adapter");

        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        mSocket.connect();
        OutputStream os = mSocket.getOutputStream();
        sender = new PrintStream(os);

        Log.e("createSocket", "Fertig, " + "Socket: " + mSocket + " Sender: " + sender + " OutputStream: " + os + " mDevice: " + mDevice.getName());
    }

    private static void fermerConnexion() {
        try {
            sendMessage("STOP");
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}