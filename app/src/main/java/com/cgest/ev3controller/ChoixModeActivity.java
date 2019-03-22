package com.cgest.ev3controller;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    // Numéro de l'image à afficher dans le pop-up de présentation des capteurs du robot.
    private int idEtapeIntroCapteurs;

    // Boolean permettant de passer l'étape de la connexion au robot.
    private boolean DEBUG = true;

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
                CustomDialog dialogAPropos = new CustomDialog(activity, "À propos", getResources().getString(R.string.credits));
                dialogAPropos.show();
                dialogAPropos.afficherBtnNegatif("Fermer");
                dialogAPropos.getTextVDialogMessage().setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dialogAPropos.getTextVDialogMessage().setText(Html.fromHtml(getResources().getString(R.string.credits), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    dialogAPropos.getTextVDialogMessage().setText(Html.fromHtml(getResources().getString(R.string.credits)));
                }

                // On adapte la hauteur du message en fonction du type de l'appareil (tablette ou smartphone).
                //   - on récupère l'échelle de l'écran.
                ViewGroup.LayoutParams param = dialogAPropos.getTextVDialogMessage().getLayoutParams();
                final float scale = getResources().getDisplayMetrics().density;
                //   - on calcule combien font 450 ou 200 pixels en dps.
                int height;
                if (Utile.isTablet(getApplicationContext())) height = 450;
                else {
                    height = 140;
                    // On met la taille de la police à 14sp sur les smartphones.
                    dialogAPropos.getTextVDialogMessage().setTextSize(14);
                }
                param.height = (int) (height * scale + 0.5f);
                // On fixe la hauteur.
                dialogAPropos.getTextVDialogMessage().setLayoutParams(param);
                dialogAPropos.getTextVDialogMessage().setMovementMethod(new ScrollingMovementMethod());
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

        if (!Utile.connexionBluetoothEtablie && !DEBUG) {
            // On lance la recherche du robot sur un autre thread.
            new InitBluetoothTask().execute((ChoixModeActivity) activity);
            Utile.connexionBluetoothEtablie = true;
        } else {
            cacherErreurBluetooth();
            afficherModes();
        }

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

    public void afficherIntroCapteursRobot() {
        // On créer le pop-up de présentation des capteurs du robot.
        final CustomDialog dialogPresentationCapteurs = new CustomDialog(this);
        dialogPresentationCapteurs.show();
        dialogPresentationCapteurs.setTitre("Présentation des capteurs du robot EV3");
        dialogPresentationCapteurs.getTextVDialogMessage().setVisibility(View.GONE);
        dialogPresentationCapteurs.getImgVDialogImage().setVisibility(View.VISIBLE);
        dialogPresentationCapteurs.getImgVDialogImage().setImageDrawable(getResources().getDrawable(R.drawable.intro_0));
        dialogPresentationCapteurs.afficherBtnPositif("Suivant");
        dialogPresentationCapteurs.afficherBtnNegatif("Précédent");
        dialogPresentationCapteurs.getBtnNegatif().setVisibility(View.INVISIBLE);
        dialogPresentationCapteurs.afficherBtnOptionnel("Fermer");

        // On fixe la taille de l'image à 420 dp sur les tablettes, et à 200dp sur les smartphones.
        Utile.setDpHeight(this, dialogPresentationCapteurs.getImgVDialogImage(), Utile.isTablet(activity) ? 420 : 150);

        idEtapeIntroCapteurs = 0;
        dialogPresentationCapteurs.getBtnPositif().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idEtapeIntroCapteurs < 6) {
                    // On récupère le nom de l'image suivante à partir du numéro de l'image.
                    String nomImageCapteur = "intro_" + ++idEtapeIntroCapteurs;
                    // On affiche l'image du capteur.
                    dialogPresentationCapteurs.getImgVDialogImage().setImageDrawable(getResources().getDrawable(Utile.getIdDrawableAvecNom(nomImageCapteur)));
                    // Si on est à la dernière image, on afficher "Fermer" à la place de "Suivant".
                    if (idEtapeIntroCapteurs == 6)
                        dialogPresentationCapteurs.getBtnPositif().setText("Fermer");
                    // On affiche le bouton "Précédent".
                    dialogPresentationCapteurs.getBtnNegatif().setVisibility(View.VISIBLE);
                } else { // S'il n'y a plus d'image...
                    // On ferme le pop-up.
                    dialogPresentationCapteurs.dismiss();
                }
            }
        });

        dialogPresentationCapteurs.getBtnNegatif().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On récupère le nom de l'image suivante à partir du numéro de l'image.
                String nomImageCapteur = "intro_" + --idEtapeIntroCapteurs;
                // On affiche l'image du capteur.
                dialogPresentationCapteurs.getImgVDialogImage().setImageDrawable(getResources().getDrawable(Utile.getIdDrawableAvecNom(nomImageCapteur)));
                // On affiche le bouton précédent si nécessaire.
                if (idEtapeIntroCapteurs - 1 < 0)
                    dialogPresentationCapteurs.getBtnNegatif().setVisibility(View.INVISIBLE);
                // On affiche le bouton "Suivant".
                dialogPresentationCapteurs.getBtnPositif().setText("Suivant");
                dialogPresentationCapteurs.getBtnPositif().setVisibility(View.VISIBLE);
            }
        });

        dialogPresentationCapteurs.getBtnOptionnel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPresentationCapteurs.dismiss();
            }
        });

    }

}