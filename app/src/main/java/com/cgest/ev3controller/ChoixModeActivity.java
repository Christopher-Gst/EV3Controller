package com.cgest.ev3controller;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ChoixModeActivity extends AppCompatActivity {

    // Contrôles utilisateur.
    private Button btnModeManuel;
    private Button btnModeAuto;
    private TextView textVChoixModeManuel;
    private TextView textVChoixModeAuto;
    private Button btnChoixModeContinuer;
    private ImageView imgVCocheModeManuel;
    private ImageView imgVCocheModeAuto;

    private boolean isModeManuelSelectionne;

    private static final int MODE_MANUEL = 0;
    private static final int MODE_AUTOMATIQUE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_mode);

        // On récupère les Views de l'activité.
        btnModeManuel = (Button) findViewById(R.id.btnModeManuel);
        btnModeAuto = (Button) findViewById(R.id.btnModeAuto);
        textVChoixModeManuel = (TextView) findViewById(R.id.textVChoixModeManuel);
        textVChoixModeAuto = (TextView) findViewById(R.id.textVChoixModeAuto);
        btnChoixModeContinuer = (Button) findViewById(R.id.btnChoixModeContinuer);
        imgVCocheModeManuel = (ImageView) findViewById(R.id.imgVCocheModeManuel);
        imgVCocheModeAuto = (ImageView) findViewById(R.id.imgVCocheModeAuto);

        // On affiche la police de caractère de l'application sur les Views.
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/fontdinerdotcom_huggable.TTF");
        btnModeManuel.setTypeface(typeface);
        btnModeAuto.setTypeface(typeface);
        textVChoixModeManuel.setTypeface(typeface);
        textVChoixModeAuto.setTypeface(typeface);
        btnChoixModeContinuer.setTypeface(typeface);

        // Le mode manuel est seléctionné par défaut.
        selectionnerMode(MODE_MANUEL);

        // On gère la sélection d'un mode.
        btnModeManuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionnerMode(MODE_MANUEL);
            }
        });

        btnModeAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionnerMode(MODE_AUTOMATIQUE);
            }
        });

    }

    // Permet de gérer l'affichage de la sélection d'un mode.
    // 0 : mode manuel.
    // 1 : mode automatique.
    private void selectionnerMode(int mode) {
        switch (mode) {
            case MODE_MANUEL:
                // On sélectionne le mode manuel.
                isModeManuelSelectionne = true;
                // On gère l'affichage des boutons et des coches.
                imgVCocheModeManuel.setVisibility(View.VISIBLE);
                btnModeManuel.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_background_home_pressed));
                imgVCocheModeAuto.setVisibility(View.GONE);
                btnModeAuto.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_background_home_unfocused));
                break;
            case MODE_AUTOMATIQUE:
                // On sélectionne le mode automatique.
                isModeManuelSelectionne = false;
                // On gère l'affichage des boutons et des coches.
                imgVCocheModeManuel.setVisibility(View.GONE);
                btnModeManuel.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_background_home_unfocused));
                imgVCocheModeAuto.setVisibility(View.VISIBLE);
                btnModeAuto.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_background_home_pressed));
                break;
        }
    }
}