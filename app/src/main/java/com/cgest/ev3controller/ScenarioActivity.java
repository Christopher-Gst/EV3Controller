package com.cgest.ev3controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cgest.ev3controller.scenario.EtapeAvancer;
import com.cgest.ev3controller.scenario.EtapeReculer;
import com.cgest.ev3controller.scenario.Scenario;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Set;
import java.util.UUID;

public class ScenarioActivity extends AppCompatActivity {

    private final static String TAG = "ScenarioActivity";

    // Variables utilisées pour l'usage du Bluetooth.
    private BluetoothAdapter mBluetoothAdapter;
    private static BluetoothDevice mDevice;
    private final static String MY_UUID = "00001101-0000-1000-8000-00805f9b34fb";
    private static BluetoothSocket mSocket = null;
    private static String mMessage = "Stop";
    private static PrintStream sender;

    // Boutons de gestion du scénario.
    private Button btnScenarioLancer;
    private Button btnScenarioEnregistrer;

    // Boutons corresponsant aux instructions à ajouter au scénario.
    private Button btnAjouterTacheAvancer;
    private Button btnAjouterTacheReculer;
    private Button btnAjouterTacheTourner;
    private Button btnAjouterTachePause;
    private Button btnAjouterTacheJouerSon;

    // RecyclerView contenant la liste visuelle de étapes du scénario.
    private RecyclerView recyclerVScenario;

    // Adapter de la RecyclerView.
    private RecyclerViewAdapterScenario adapter;

    // Scénario édité ou créée par l'utilisateur.
    Scenario sc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario);

        // On associe les variables des views au views en ressources.
        btnScenarioLancer = (Button) findViewById(R.id.btnScenarioLancer);
        btnScenarioEnregistrer = (Button) findViewById(R.id.btnScenarioEnregistrer);

        btnAjouterTacheAvancer = (Button) findViewById(R.id.btnAjouterTacheAvancer);
        btnAjouterTacheReculer = (Button) findViewById(R.id.btnAjouterTacheReculer);
        btnAjouterTacheTourner = (Button) findViewById(R.id.btnAjouterTacheTourner);
        btnAjouterTachePause = (Button) findViewById(R.id.btnAjouterTachePause);
        btnAjouterTacheJouerSon = (Button) findViewById(R.id.btnAjouterTacheJouerSon);

        recyclerVScenario = (RecyclerView) findViewById(R.id.recyclerVScenario);

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

        btnScenarioLancer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.e(TAG, sc.getCode());
                    sendMessage(sc.getCode());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Initialisation de la RecyclerView.
        initRecyclerView();

        sc = new Scenario();
        sc.ajouterEtape(new EtapeAvancer(50, EtapeAvancer.CM));
        sc.ajouterEtape(new EtapeReculer(3, EtapeAvancer.SECONDES));
        //sc.ajouterEtape(new EtapeMusique());
        //sc.ajouterEtape(new EtapeRotation(90, EtapeRotation.GAUCHE));
        //sc.ajouterEtape(new EtapeAvancer(new CapteurProximite(30)));
        //sc.ajouterEtape(new EtapeMusique());
        //sc.ajouterEtape(new EtapeReculer(20, EtapeReculer.CM, new CapteurToucher()));

        adapter.setScenario(sc);
    }

    public void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerVScenario);
        adapter = new RecyclerViewAdapterScenario(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
            btnScenarioLancer.setClickable(false);
        } else {
            Log.e(TAG, "Bluetooth supported");
        }
        if (!mBluetoothAdapter.isEnabled()) {
            btnScenarioLancer.setClickable(false);
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
