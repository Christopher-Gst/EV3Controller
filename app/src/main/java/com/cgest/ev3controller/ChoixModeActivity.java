package com.cgest.ev3controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class ChoixModeActivity extends AppCompatActivity {

    // Contexte de l'activité.
    private Activity activity;

    // Contrôles utilisateur.
    private Button btnModeManuel;
    private Button btnModeScan;
    private Button btnModeAuto;
    private Button imgBtnChoixModeCredits;
    // ConstraintLayout contenant toutes les views sur les modes (boutons, descriptions, ...).
    private ConstraintLayout constraintLModes;
    // Views liées à l'activation du Bluetooth ou à la connexion au robot.
    private TextView textVChoixModeErreurBluetooth;
    private ImageView imageVChoixModeErreurBluetooth;
    private ProgressBar progressBarConnexionBluetooth;
    private FrameLayout btnChoixModeReessayerConnexion;

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
        constraintLModes = (ConstraintLayout) findViewById(R.id.constraintLModes);
        btnModeManuel = (Button) findViewById(R.id.btnModeManuel);
        btnModeScan = (Button) findViewById(R.id.btnModeScan);
        btnModeAuto = (Button) findViewById(R.id.btnModeAuto);
        imgBtnChoixModeCredits = (Button) findViewById(R.id.btnChoixModeCredits);
        // Views liées à l'activation du Bluetooth ou à la connexion au robot.
        textVChoixModeErreurBluetooth = (TextView) findViewById(R.id.textVChoixModeErreurBluetooth);
        imageVChoixModeErreurBluetooth = (ImageView) findViewById(R.id.imageVChoixModeErreurBluetooth);
        progressBarConnexionBluetooth = (ProgressBar) findViewById(R.id.progressBarConnexionBluetooth);
        btnChoixModeReessayerConnexion = (FrameLayout) findViewById(R.id.btnChoixModeReessayerConnexion);

        // On affiche la police de caractère de l'application ("Fontdinerdotcom Huggable") sur les Views.
        Utile.appliquerPolicePrincipale(this, btnModeManuel);
        Utile.appliquerPolicePrincipale(this, btnModeScan);
        Utile.appliquerPolicePrincipale(this, btnModeAuto);
        Utile.appliquerPolicePrincipale(this, textVChoixModeErreurBluetooth);
        Utile.appliquerPolicePrincipale(this, (Button) btnChoixModeReessayerConnexion.getChildAt(0));

        // On gère la sélection d'un mode.
        btnModeManuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentNextActivity = new Intent(ChoixModeActivity.this, EditionScenarioActivity.class);
                intentNextActivity.putExtra("MODE", "manuel");
                startActivity(intentNextActivity);
            }
        });

        btnModeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Si l'application "Barcode Scanner" n'est pas installé sur l'appareil, on prévient l'utilisateur,
                // en lui demandant de télécharger l'application.
                if (!Utile.scannerQrCodeInstalle(activity)) {
                    // On crée une pop-up.
                    final CustomDialog dialogTelechargerScanner = new CustomDialog(activity, "Mode indisponible", getResources().getString(R.string.mode_scan_indisponible));
                    dialogTelechargerScanner.show();
                    // On affiche des boutons "Oui" et "Non" et on spécifie l'action du "Oui".
                    dialogTelechargerScanner.afficherBtnPositif("Oui");
                    dialogTelechargerScanner.afficherBtnNegatif("Non");
                    dialogTelechargerScanner.getBtnPositif().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogTelechargerScanner.dismiss();
                            // On ouvre le Google Play Store sur la page de Barcode Scanner.
                            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                            startActivity(marketIntent);
                        }
                    });
                } else {
                    // Si l'application est installée, on affiche le mode scan.
                    Intent intentNextActivity = new Intent(ChoixModeActivity.this, EditionScenarioActivity.class);
                    intentNextActivity.putExtra("MODE", "scan");
                    startActivity(intentNextActivity);
                }
            }
        });

        btnModeAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChoixModeActivity.this, ModeAutoActivity.class));
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

        btnChoixModeReessayerConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageVChoixModeErreurBluetooth.setVisibility(View.INVISIBLE);
                progressBarConnexionBluetooth.setVisibility(View.VISIBLE);
                textVChoixModeErreurBluetooth.setText("Recherche du robot...");
                btnChoixModeReessayerConnexion.setVisibility(View.INVISIBLE);
                new InitBluetoothTask().execute((ChoixModeActivity) activity);
            }
        });

        /*if (!Utile.connexionBluetoothEtablie) {
            // On lance la recherche du robot sur un autre thread.
            new InitBluetoothTask().execute((ChoixModeActivity) activity);
            Utile.connexionBluetoothEtablie = true;
        } else {*/
            cacherErreurBluetooth();
            afficherModes();
        //}

    }

    /*
    Permet d'activer le fait de pouvoir sélectionner un mode, après que l'activation du Bluetooth et la connexion
    au robot aient été effectives.
     */
    public void afficherModes() {
        constraintLModes.setVisibility(View.VISIBLE);
    }

    /*
    Permet d'afficher une erreur relative à l'activation du Bluetooth ou à la connexion au robot.
     */
    public void afficherErreurBluetooth(String message) {
        progressBarConnexionBluetooth.setVisibility(View.INVISIBLE);
        imageVChoixModeErreurBluetooth.setVisibility(View.VISIBLE);
        textVChoixModeErreurBluetooth.setText(message);
        btnChoixModeReessayerConnexion.setVisibility(View.VISIBLE);
    }

    /*
    Permet de cacher une erreur relative à l'activation du Bluetooth ou à la connexion au robot.
     */
    public void cacherErreurBluetooth() {
        progressBarConnexionBluetooth.setVisibility(View.INVISIBLE);
        imageVChoixModeErreurBluetooth.setVisibility(View.INVISIBLE);
        textVChoixModeErreurBluetooth.setVisibility(View.INVISIBLE);
        btnChoixModeReessayerConnexion.setVisibility(View.INVISIBLE);
    }

}