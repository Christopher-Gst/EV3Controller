package com.cgest.ev3controller;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
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
    private TextView textVEditionScenarioTitre;
    private TextView textVActions;
    private TextView textVEditionScenarioNomDuScenario;

    private ConstraintLayout constraintLScenario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edition_scenario);

        // On récupère les vues de l'activity.
        // - TextView "Mode manuel" ou "Mode scan" en haut de l'interface.
        textVEditionScenarioTitre = (TextView) findViewById(R.id.textVEditionScenarioTitre);
        textVActions = (TextView) findViewById(R.id.textVActions);
        textVEditionScenarioNomDuScenario = (TextView) findViewById(R.id.textVEditionScenarioNomDuScenario);
        constraintLScenario = (ConstraintLayout) findViewById(R.id.constraintLScenario);

        // On affiche la police de caractère de l'application ("Fontdinerdotcom Huggable") sur les Views.
        Utile.appliquerPolicePrincipale(this, textVEditionScenarioTitre);
        Utile.appliquerPolicePrincipale(this, textVActions);
        Utile.appliquerPolicePrincipale(this, textVEditionScenarioNomDuScenario);

        textVEditionScenarioTitre.setText("Mode Manuel");
        textVEditionScenarioNomDuScenario.setText("Scénario de test");

        for (int i = 0; i < constraintLScenario.getChildCount(); i++) {
            final View child = constraintLScenario.getChildAt(i);
            if (child instanceof TextView) {
                TextView tv = (TextView) child;
                Utile.appliquerPolicePrincipale(this, tv);
            }
        }

    }
}