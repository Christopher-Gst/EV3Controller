package com.cgest.ev3controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ChoixModeActivity extends AppCompatActivity {

    // Contexte de l'activité.
    private Activity activity;

    // Contrôles utilisateur.
    private Button btnModeManuel;
    private Button btnModeScan;
    private Button btnModeAuto;
    private TextView textVChoixModeManuel;
    private TextView textVChoixModeScan;
    private TextView textVChoixModeAuto;
    private Button btnChoixModeContinuer;
    private ImageView imgVCocheModeManuel;
    private ImageView imgVCocheModeScan;
    private ImageView imgVCocheModeAuto;
    private Button imgBtnChoixModeCredits;

    private int modeSelectionne;

    private static final int MODE_MANUEL = 0;
    private static final int MODE_SCAN = 1;
    private static final int MODE_AUTOMATIQUE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_mode);

        // On récupère le contexte actuel.
        activity = this;

        // On récupère les Views de l'activité.
        btnModeManuel = (Button) findViewById(R.id.btnModeManuel);
        btnModeScan = (Button) findViewById(R.id.btnModeScan);
        btnModeAuto = (Button) findViewById(R.id.btnModeAuto);
        textVChoixModeManuel = (TextView) findViewById(R.id.textVChoixModeManuel);
        textVChoixModeScan = (TextView) findViewById(R.id.textVChoixModeScan);
        textVChoixModeAuto = (TextView) findViewById(R.id.textVChoixModeAuto);
        btnChoixModeContinuer = (Button) findViewById(R.id.btnChoixModeContinuer);
        imgVCocheModeManuel = (ImageView) findViewById(R.id.imgVCocheModeManuel);
        imgVCocheModeScan = (ImageView) findViewById(R.id.imgVCocheModeScan);
        imgVCocheModeAuto = (ImageView) findViewById(R.id.imgVCocheModeAuto);
        imgBtnChoixModeCredits = (Button) findViewById(R.id.btnChoixModeCredits);

        // On affiche la police de caractère de l'application ("Fontdinerdotcom Huggable") sur les Views.
        Utile.appliquerPolicePrincipale(this, btnModeManuel);
        Utile.appliquerPolicePrincipale(this, btnModeScan);
        Utile.appliquerPolicePrincipale(this, btnModeAuto);
        Utile.appliquerPolicePrincipale(this, textVChoixModeManuel);
        Utile.appliquerPolicePrincipale(this, textVChoixModeScan);
        Utile.appliquerPolicePrincipale(this, textVChoixModeAuto);
        Utile.appliquerPolicePrincipale(this, btnChoixModeContinuer);

        // Le mode manuel est seléctionné par défaut.
        selectionnerMode(MODE_MANUEL);

        // On gère la sélection d'un mode.
        btnModeManuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionnerMode(MODE_MANUEL);
            }
        });

        btnModeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionnerMode(MODE_SCAN);
            }
        });

        btnModeAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionnerMode(MODE_AUTOMATIQUE);
            }
        });

        // Gestion du bouton "A propos".
        imgBtnChoixModeCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog dialogAPropos = new CustomDialog(activity, "À propos", "Texte à définir.");
                dialogAPropos.show();
                dialogAPropos.afficherBtnNegatif("Fermer");
            }
        });

        // On gère l'ouverture du mode seléctionné par l'utilisateur.
        btnChoixModeContinuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Le mode automatique est sélectionné.
                Intent intentNextActivity = null;
                if (modeSelectionne == MODE_MANUEL) {
                    intentNextActivity = new Intent(ChoixModeActivity.this, EditionScenarioActivity.class);
                } else if (modeSelectionne == MODE_SCAN) {
                    intentNextActivity = new Intent(ChoixModeActivity.this, EditionScenarioActivity.class);
                } else if (modeSelectionne == MODE_AUTOMATIQUE) {
                    intentNextActivity = new Intent(ChoixModeActivity.this, ModeAutoActivity.class);
                }
                startActivity(intentNextActivity);
            }
        });

        // On initialise la connexion avec le robot.
        Ev3BluetoothManager.initialiserLeManager();

    }

    // Permet de gérer l'affichage de la sélection d'un mode.
    // 0 : mode manuel.
    // 1 : mode scan.
    // 2 : mode automatique.
    private void selectionnerMode(int mode) {

        // On sélectionne le bon mode en gérant l'affichage des coches et des boutons de sélection.
        switch (mode) {
            case MODE_MANUEL:
                // On sélectionne le mode manuel.
                modeSelectionne = MODE_MANUEL;
                // On rend visible la coche du mode manuel et on éclaircie le bouton de ce mode.
                imgVCocheModeManuel.setVisibility(View.VISIBLE);
                btnModeManuel.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_background_home_pressed));
                break;
            case MODE_SCAN:
                // On sélectionne le mode scan.
                modeSelectionne = MODE_SCAN;
                // On rend visible la coche du mode scan et on éclaircie le bouton de ce mode.
                imgVCocheModeScan.setVisibility(View.VISIBLE);
                btnModeScan.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_background_home_pressed));
                break;
            case MODE_AUTOMATIQUE:
                // On sélectionne le mode scan.
                modeSelectionne = MODE_AUTOMATIQUE;
                // On rend visible la coche du mode automatique et on éclaircie le bouton de ce mode.
                imgVCocheModeAuto.setVisibility(View.VISIBLE);
                btnModeAuto.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_background_home_pressed));
                break;
        }

        // On cache la coche et le bouton de sélection des autres modes.
        // - mode manuel :
        if (mode != MODE_MANUEL) {
            imgVCocheModeManuel.setVisibility(View.GONE);
            btnModeManuel.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_background_home_unfocused));
        }
        // - mode scan :
        if (mode != MODE_SCAN) {
            imgVCocheModeScan.setVisibility(View.GONE);
            btnModeScan.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_background_home_unfocused));
        }
        // - mode automatique :
        if (mode != MODE_AUTOMATIQUE) {
            imgVCocheModeAuto.setVisibility(View.GONE);
            btnModeAuto.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_background_home_unfocused));
        }

    }
}