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
                        Log.e(TAG, "ACTION_DOWN");
                        Scenario scenario = new Scenario();
                        // On arrange le sens du robot pour l'orienter vers l'avant.
                        if (sensRobot != 0) {
                            int angleDeCorrection = 0;
                            switch (sensRobot) {
                                case -1:
                                    angleDeCorrection = 90;
                                    break;
                                case 1:
                                    angleDeCorrection = -90;
                                    break;
                            }
                            sensRobot = 0;
                            EtapeRotation etapeRot = new EtapeRotation(EtapeRotation.DROITE, angleDeCorrection);
                            scenario.ajouterEtape(etapeRot);
                        }
                        // On fait avancer le robot.
                        EtapeAvancer etapeRouler = new EtapeAvancer(-1, -1);
                        scenario.ajouterEtape(etapeRouler);

                        // On envoie le scénario au robot.
                        Ev3BluetoothManager.envoyerScenario(scenario);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e(TAG, "ACTION_UP");
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
                        Log.e(TAG, "ACTION_DOWN");
                        Scenario scenario = new Scenario();
                        // On arrange le sens du robot pour l'orienter vers l'avant.
                        if (sensRobot != 0) {
                            int angleDeCorrection = 0;
                            switch (sensRobot) {
                                case -1:
                                    angleDeCorrection = 90;
                                    break;
                                case 1:
                                    angleDeCorrection = -90;
                                    break;
                            }
                            sensRobot = 0;
                            EtapeRotation etapeRot = new EtapeRotation(EtapeRotation.DROITE, angleDeCorrection);
                            scenario.ajouterEtape(etapeRot);
                        }
                        // On fait avancer le robot.
                        EtapeReculer etapeRouler = new EtapeReculer(-1, -1);
                        scenario.ajouterEtape(etapeRouler);

                        // On envoie le scénario au robot.
                        Ev3BluetoothManager.envoyerScenario(scenario);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e(TAG, "ACTION_UP");
                        envoyerCommandeArret();
                        break;
                }
                return true;
            }
        });

        btnModeAutoDroite.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e(TAG, "ACTION_DOWN");
                        Scenario scenario = new Scenario();
                        // On arrange le sens du robot pour l'orienter vers la droite.
                        if (sensRobot != 1) {
                            int angleDeCorrection = 0;
                            switch (sensRobot) {
                                case 0:
                                    angleDeCorrection = 90;
                                    break;
                                case -1:
                                    angleDeCorrection = 180;
                                    break;
                            }
                            sensRobot = 1;
                            EtapeRotation etapeRot = new EtapeRotation(EtapeRotation.DROITE, angleDeCorrection);
                            scenario.ajouterEtape(etapeRot);
                        }
                        // On fait avancer le robot.
                        EtapeAvancer etapeRouler = new EtapeAvancer(-1, -1);
                        scenario.ajouterEtape(etapeRouler);

                        // On envoie le scénario au robot.
                        Ev3BluetoothManager.envoyerScenario(scenario);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e(TAG, "ACTION_UP");
                        envoyerCommandeArret();
                        break;
                }
                return true;
            }
        });

        btnModeAutoGauche.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e(TAG, "ACTION_DOWN");
                        Scenario scenario = new Scenario();
                        // On arrange le sens du robot pour l'orienter vers la gauche.
                        if (sensRobot != -1) {
                            int angleDeCorrection = 0;
                            switch (sensRobot) {
                                case 0:
                                    angleDeCorrection = 90;
                                    break;
                                case 1:
                                    angleDeCorrection = 180;
                                    break;
                            }
                            sensRobot = -1;
                            EtapeRotation etapeRot = new EtapeRotation(EtapeRotation.GAUCHE, angleDeCorrection);
                            scenario.ajouterEtape(etapeRot);
                        }
                        // On fait avancer le robot.
                        EtapeAvancer etapeRouler = new EtapeAvancer(-1, -1);
                        scenario.ajouterEtape(etapeRouler);

                        // On envoie le scénario au robot.
                        Ev3BluetoothManager.envoyerScenario(scenario);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e(TAG, "ACTION_UP");
                        envoyerCommandeArret();
                        break;
                }
                return true;
            }
        });

    }

    private void envoyerCommandeArret() {
        Scenario scenario = new Scenario();
        EtapeArreter etapeArreter = new EtapeArreter();
        scenario.ajouterEtape(etapeArreter);
        Ev3BluetoothManager.envoyerScenario(scenario);
    }

}