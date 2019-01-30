package com.cgest.ev3controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cgest.ev3controller.capteur.CapteurCouleur;
import com.cgest.ev3controller.capteur.CapteurProximite;
import com.cgest.ev3controller.capteur.CapteurToucher;
import com.cgest.ev3controller.capteur.Couleur;
import com.cgest.ev3controller.scenario.Etape;
import com.cgest.ev3controller.scenario.EtapeAvancer;
import com.cgest.ev3controller.scenario.EtapeAvancerReculer;
import com.cgest.ev3controller.scenario.EtapeMusique;
import com.cgest.ev3controller.scenario.EtapePause;
import com.cgest.ev3controller.scenario.EtapeReculer;
import com.cgest.ev3controller.scenario.EtapeRotation;
import com.cgest.ev3controller.scenario.Scenario;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    private BluetoothAdapter mBluetoothAdapter;
    private static BluetoothDevice mDevice;
    private Button btnSendMessage;
    private EditText txtMessage;

    private final static String MY_UUID = "00001101-0000-1000-8000-00805f9b34fb";
    private static BluetoothSocket mSocket = null;
    private static String mMessage = "Stop";
    private static PrintStream sender;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        try {
            Scenario sc = new Scenario();
            sc.ajouterEtape(new EtapeAvancer(50, EtapeAvancer.CM));
            //sc.ajouterEtape(new EtapeMusique());
            //sc.ajouterEtape(new EtapeRotation(90, EtapeRotation.GAUCHE));
            //sc.ajouterEtape(new EtapeAvancer(new CapteurProximite(30)));
            //sc.ajouterEtape(new EtapeMusique());
            //sc.ajouterEtape(new EtapeReculer(20, EtapeReculer.CM, new CapteurToucher()));
            String code = sc.getCode();
            Log.e(TAG, code);
            sendMessage(code);
        } catch (IOException e) {
            e.printStackTrace();
        }

        txtMessage = (EditText) findViewById(R.id.txtMessage);
        btnSendMessage = (Button) findViewById(R.id.btnEnvoyer);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (findBrick()) {
                    Log.e(TAG, "Connexion réussie");
                    try {
                        sendMessage(txtMessage.getText().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "Le robot n'a pas été trouvé.");
                }
            }
        });

    }

    private boolean findBrick() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            if (device.getName().equals("EV3")) {
                this.mDevice = device;
                return true;
            }
        }
        return false;
    }

    private void initBluetooth() {
        Log.e(TAG, "Checking Bluetooth...");
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Device does not support Bluetooth");
            btnSendMessage.setClickable(false);
        } else {
            Log.e(TAG, "Bluetooth supported");
        }
        if (!mBluetoothAdapter.isEnabled()) {
            btnSendMessage.setClickable(false);
            Log.e(TAG, "Bluetooth not enabled");
        } else {
            Log.e(TAG, "Bluetooth enabled");
        }
    }

    public static void sendMessage(String message) throws IOException {
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

    public void createSocket() throws IOException {
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

    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy", "App beendet");
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("onDestroy", "App vollständig beendet");
    }
}