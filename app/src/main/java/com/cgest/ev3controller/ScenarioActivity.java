package com.cgest.ev3controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cgest.ev3controller.scenario.EtapeAvancer;
import com.cgest.ev3controller.scenario.EtapeMusique;
import com.cgest.ev3controller.scenario.EtapePause;
import com.cgest.ev3controller.scenario.EtapeReculer;
import com.cgest.ev3controller.scenario.EtapeRotation;
import com.cgest.ev3controller.scenario.Scenario;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collections;
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
    private RecyclerView recyclerVScenarioOld;

    // Adapter de la RecyclerView.
    private RecyclerViewAdapterScenarioOld adapter;

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

        recyclerVScenarioOld = (RecyclerView) findViewById(R.id.recyclerVScenarioOld);

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
                    Log.e(TAG, adapter.scenario.getCode());
                    // On envoie le scénario au robot. On le fait deux fois à cause de l'instabilité du comportement du robot face au bluetooth.
                    sendMessage(adapter.scenario.getCode());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendMessage(adapter.scenario.getCode());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Boutons permettant d'ajouter une étape au scénario.
        btnAjouterTacheAvancer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.scenario.getEtapes().add(new EtapeAvancer());
                adapter.notifyItemInserted(adapter.scenario.getEtapes().size() - 1);
            }
        });

        btnAjouterTacheReculer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.scenario.getEtapes().add(new EtapeReculer());
                adapter.notifyItemInserted(adapter.scenario.getEtapes().size() - 1);
            }
        });

        btnAjouterTachePause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.scenario.getEtapes().add(new EtapePause());
                adapter.notifyItemInserted(adapter.scenario.getEtapes().size() - 1);
            }
        });

        btnAjouterTacheJouerSon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.scenario.getEtapes().add(new EtapeMusique());
                adapter.notifyItemInserted(adapter.scenario.getEtapes().size() - 1);
            }
        });

        btnAjouterTacheTourner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.scenario.getEtapes().add(new EtapeRotation());
                adapter.notifyItemInserted(adapter.scenario.getEtapes().size() - 1);
            }
        });

        // Initialisation de la RecyclerView.
        initRecyclerView();
    }

    public void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerVScenarioOld);
        adapter = new RecyclerViewAdapterScenarioOld(this, recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Extend the Callback class
        ItemTouchHelper.Callback _ithCallback = new ItemTouchHelper.Callback() {
            //and in your imlpementaion of
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // get the viewHolder's and target's positions in your adapter data, swap them
                Collections.swap(adapter.scenario.getEtapes(), viewHolder.getAdapterPosition(), target.getAdapterPosition());
                // and notify the adapter that its dataset has changed
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                adapter.colorerLignes();
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //TODO
            }

            //defines the enabled move directions in each state (idle, swiping, dragging).
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
            }
        };

        // Create an `ItemTouchHelper` and attach it to the `RecyclerView`
        ItemTouchHelper ith = new ItemTouchHelper(_ithCallback);
        ith.attachToRecyclerView(recyclerView);
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
        try {
            sendMessage("STOP");
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}