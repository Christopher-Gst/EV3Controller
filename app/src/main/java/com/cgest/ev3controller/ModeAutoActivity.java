package com.cgest.ev3controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cgest.ev3controller.scenario.EtapeArreter;
import com.cgest.ev3controller.scenario.EtapeAvancer;
import com.cgest.ev3controller.scenario.Scenario;

public class ModeAutoActivity extends AppCompatActivity {

    // TAG de cette activité utilisé pour le Logcat.
    private final static String TAG = "ModeAutoActivity";

    // Contrôles utilisateur.
    private TextView textVModeAutoTitre;
    // Boutons des flèches directionnelles de contrôles du robot.
    private Button btnModeAutoHaut;
    private Button btnModeAutoBas;
    private Button btnModeAutoGauche;
    private Button btnModeAutoDroite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mode_automatique);

        // On récupère les vues de l'activity.
        // - TextView "Mode automatique" en haut de l'interface.
        textVModeAutoTitre = (TextView) findViewById(R.id.textVModeAutoTitre);
        // -  Boutons des flèches directionnelles de contrôles du robot.
        btnModeAutoHaut = (Button) findViewById(R.id.btnModeAutoHaut);
        btnModeAutoBas = (Button) findViewById(R.id.btnModeAutoBas);
        btnModeAutoGauche = (Button) findViewById(R.id.btnModeAutoGauche);
        btnModeAutoDroite = (Button) findViewById(R.id.btnModeAutoDroite);

        // On affiche la police de caractère de l'application ("Fontdinerdotcom Huggable") sur les Views.
        Utile.appliquerPolicePrincipale(this, textVModeAutoTitre);

        // On gère l'appui et le relâchement des boutons.
        btnModeAutoHaut.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Scenario scenario = new Scenario();
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e(TAG, "ACTION_DOWN");
                        EtapeAvancer etapeAvancer = new EtapeAvancer(-1, -1);
                        scenario.ajouterEtape(etapeAvancer);
                        Ev3BluetoothManager.envoyerScenario(scenario);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e(TAG, "ACTION_UP");
                        EtapeArreter etapeArreter = new EtapeArreter();
                        scenario.ajouterEtape(etapeArreter);
                        Ev3BluetoothManager.envoyerScenario(scenario);
                        break;
                }
                return true;
            }
        });

    }
}