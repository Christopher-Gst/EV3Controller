package com.cgest.ev3controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.drm.DrmStore;
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

    public static String initialiserLeManager(ChoixModeActivity activity) {
        // On ititialise la connexion Bluetooth avec le robot et on créer le socket de communication.
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String messageSurInterface = null;
        // Si l'initialisation du Bluetooth ne renvoie pas d'erreur...
        if ((messageSurInterface = initBluetooth()) == null) {
            if (findBrick()) {
                // Si la création du socket ne renvoie pas d'erreur...
                if (!createSocket())
                    messageSurInterface = "Le robot est bien appairé avec l'appareil, mais la connexion n'est pas établie." +
                            " Assurez-vous que :\n- l'appareil est connecté au robot. La connexion doit être faite manuellement depuis" +
                            " les paramètres de l'appareil et du robot.\n- Le programme d'écoute est lancé sur le robot.";
            } else {
                messageSurInterface = "Le robot n'est pas appairé avec cet appareil. Vous devez l'appairer pour pouvoir établir la connexion.";
            }
        }
        return messageSurInterface;
    }

    public static void envoyerScenario(Scenario scenario) {
        Log.e(TAG, "Envoi de : " + scenario.getCode());
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

    private static String initBluetooth() {
        if (mBluetoothAdapter == null)
            return "Cet appareil ne supporte pas le Bluetooth !";
        if (!mBluetoothAdapter.isEnabled())
            return "Le Bluetooth n'est pas activé !";
        else
            return null;
    }

    private static void sendMessage(String message) throws IOException {
        try {
            OutputStream os = mSocket.getOutputStream();
            sender = new PrintStream(os);
            Log.e("onSend", "Message = " + message);
            sender.println(message);
            sender.flush();
            Log.e("onSend", "Message sent");
        } catch (IllegalStateException | NullPointerException e) {
            e.printStackTrace();
        }

    }

    private static boolean createSocket() {
        try {
            UUID uuid = UUID.fromString(MY_UUID);
            mSocket = mDevice.createRfcommSocketToServiceRecord(uuid);

            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
            mSocket.connect();
            OutputStream os = mSocket.getOutputStream();
            sender = new PrintStream(os);

            Log.e("createSocket", "Prêt, " + "Socket: " + mSocket + " Sender: " + sender + " OutputStream: " + os + " mDevice: " + mDevice.getName());

            return true;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            return false;
        }
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