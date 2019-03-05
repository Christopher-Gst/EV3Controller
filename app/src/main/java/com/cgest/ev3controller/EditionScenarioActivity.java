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

public class EditionScenarioActivity extends AppCompatActivity {

    // TAG de cette activité utilisé pour le Logcat.
    private final static String TAG = "EditionScenarioActivity";

    // Contrôles utilisateur.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edition_scenario);

        // On récupère les vues de l'activity.
        // - TextView "Mode automatique" en haut de l'interface.

        // -  Boutons des flèches directionnelles de contrôles du robot.

        // On affiche la police de caractère de l'application ("Fontdinerdotcom Huggable") sur les Views.
        //Utile.appliquerPolicePrincipale(this, textVModeAutoTitre);



    }
}