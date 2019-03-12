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
import com.cgest.ev3controller.scenario.EtapeAvancerReculer;
import com.cgest.ev3controller.scenario.EtapeReculer;
import com.cgest.ev3controller.scenario.EtapeRotation;
import com.cgest.ev3controller.scenario.Scenario;

import java.io.IOException;

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

    // Sens du robot :
    //      0 : le robot est droit, dans le même sens que quand l'utilisateur a ouvert le mode automatique
    //      -1 : le robot est tourné vers la gauche, en angle droit.
    //      1 : le robot est tourné vers la droite, en angle droit.
    private int sensRobot = 0;

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
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // On crée un scénario.
                        Scenario scenario = new Scenario();

                        // On fait avancer le robot.
                        EtapeAvancer etapeRouler = new EtapeAvancer(-1, -1);
                        scenario.ajouterEtape(etapeRouler);

                        // On envoie le scénario au robot.
                        Ev3BluetoothManager.envoyerScenario(scenario);
                        break;
                    case MotionEvent.ACTION_UP:
                        envoyerCommandeArret();
                        break;
                }
                return true;
            }
        });

        btnModeAutoBas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // On crée un scénario.
                        Scenario scenario = new Scenario();

                        // On fait avancer le robot.
                        EtapeReculer etapeRouler = new EtapeReculer(-1, -1);
                        scenario.ajouterEtape(etapeRouler);

                        // On envoie le scénario au robot.
                        Ev3BluetoothManager.envoyerScenario(scenario);
                        break;
                    case MotionEvent.ACTION_UP:
                        envoyerCommandeArret();
                        break;
                }
                return true;
            }
        });

        btnModeAutoDroite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On crée un scénario.
                Scenario scenario = new Scenario();

                // On fait une rotation à 45° à droite.
                EtapeRotation etapeRotation = new EtapeRotation(EtapeRotation.DROITE, 45);
                scenario.ajouterEtape(etapeRotation);

                // On envoie le scénario au robot.
                Ev3BluetoothManager.envoyerScenario(scenario);
            }
        });

        btnModeAutoGauche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On crée un scénario.
                Scenario scenario = new Scenario();

                // On fait une rotation à 45° à gauche.
                EtapeRotation etapeRotation = new EtapeRotation(EtapeRotation.GAUCHE, 45);
                scenario.ajouterEtape(etapeRotation);

                // On envoie le scénario au robot.
                Ev3BluetoothManager.envoyerScenario(scenario);
            }
        });

    }

    private void envoyerCommandeArret() {
        Scenario scenario = new Scenario();
        EtapeArreter etapeArreter = new EtapeArreter();
        scenario.ajouterEtape(etapeArreter);
        Ev3BluetoothManager.envoyerScenario(scenario);
    }

    @Override
    public void onBackPressed() {
        // On envoie un message au robot signalant qu'on change de mode.
        try {
            Ev3BluetoothManager.sendMessage("EXIT_MODE");
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onBackPressed();
    }

}