package com.cgest.ev3controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cgest.ev3controller.capteur.Couleur;
import com.cgest.ev3controller.scenario.Etape;
import com.cgest.ev3controller.scenario.EtapeArreter;
import com.cgest.ev3controller.scenario.EtapeAvancer;
import com.cgest.ev3controller.scenario.Scenario;

public class EditionScenarioActivity extends AppCompatActivity {

    // TAG de cette activité utilisé pour le Logcat.
    private final static String TAG = "EditionScenarioActivity";

    // Contexte de l'activité.
    private Activity activity;

    // Contrôles utilisateur.
    private TextView textVEditionScenarioTitre;
    private TextView textVActions;
    private TextView textVEditionScenarioNomDuScenario;

    //private ConstraintLayout constraintLScenario;
    // LinearLayout contenant tous les boutons permettant d'ajouter des actions au scénario.
    private LinearLayout linearLayoutActions;

    // Champ de saisie affichée dans la pop-up lors de l'ajout d'un action au scénario.
    private View champDeSaisie;

    // Etape à ajouter au scénario.
    Etape etape = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edition_scenario);

        // On récupère le contexte actuel.
        activity = this;

        // On récupère les vues de l'activity.
        // - TextView "Mode manuel" ou "Mode scan" en haut de l'interface.
        textVEditionScenarioTitre = (TextView) findViewById(R.id.textVEditionScenarioTitre);
        textVActions = (TextView) findViewById(R.id.textVActions);
        textVEditionScenarioNomDuScenario = (TextView) findViewById(R.id.textVEditionScenarioNomDuScenario);
        //constraintLScenario = (ConstraintLayout) findViewById(R.id.constraintLScenario);
        linearLayoutActions = (LinearLayout) findViewById(R.id.linearLayoutActions);

        // On affiche la police de caractère de l'application ("Fontdinerdotcom Huggable") sur les Views.
        Utile.appliquerPolicePrincipale(this, textVEditionScenarioTitre);
        Utile.appliquerPolicePrincipale(this, textVActions);
        Utile.appliquerPolicePrincipale(this, textVEditionScenarioNomDuScenario);

        textVEditionScenarioTitre.setText("Mode Manuel");
        textVEditionScenarioNomDuScenario.setText("Scénario de test");

        /*for (int i = 0; i < constraintLScenario.getChildCount(); i++) {
            final View child = constraintLScenario.getChildAt(i);
            if (child instanceof TextView) {
                TextView tv = (TextView) child;
                Utile.appliquerPolicePrincipale(this, tv);
            }
        }*/

        // On applique un ClickListener à tous les boutons permettant d'ajouter des actions au scénario, dans la liste de gauche.
        for (int i = 0; i < linearLayoutActions.getChildCount(); i++) {
            final View child = linearLayoutActions.getChildAt(i);
            if (child instanceof FrameLayout) {
                final FrameLayout frameLayoutBoutonAction = (FrameLayout) child;
                frameLayoutBoutonAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // On récupère le Tag du bouton pour savoir quelle action ajouter au scénario.
                        final String tagAction = frameLayoutBoutonAction.getTag().toString();

                        // Si l'action a des paramètres à spécifier...
                        if (tagAction.contains("?")) {
                            // On affiche une boîte de dialogue avec le message et les champs de saisie adaptés, pour paramétrer
                            // l'action.
                            final CustomDialog dialogSaisieInfosAction = new CustomDialog(activity);
                            dialogSaisieInfosAction.setTitre("Ajout d'une action");
                            // On affiche la pop-up avant même d'avoir spécifié le message, les vues à afficher, ... car la pop-up
                            // a besoin de charger ses vues avant d'agir dessus (dans la méthode onCreate()).
                            dialogSaisieInfosAction.show();
                            String messageDialog = "";

                            // On affiche le bon message en fonction du type d'action :
                            switch (tagAction) {
                                case "A.?.0":
                                case "A.?.1":
                                case "R.?.0":
                                case "R.?.1":
                                    messageDialog = "Saisissez la distance de déplacement :";
                                    break;
                                case "A.P.?":
                                case "R.P.?":
                                    messageDialog = "Saisissez la distance de détection :";
                                    break;
                                case "A.C.?":
                                case "R.C.?":
                                    messageDialog = "Sélectionnez la couleur à détecter :";
                                    break;
                                case "ROT.0.?":
                                case "ROT.1.?":
                                    messageDialog = "Saisissez l'angle de rotation (90°, 180°, ...) :";
                                    break;
                                case "P.?":
                                    messageDialog = "Saisissez la durée de la pause :";
                                    break;
                            }

                            // On affiche le bon type de champ de saisie en fonction du type d'action :
                            Object actionParamType = Etape.getEtapeFromCodeNoParam(tagAction).getParamType();

                            if (actionParamType instanceof Integer) {
                                dialogSaisieInfosAction.afficherSaisieNombre();
                                champDeSaisie = (EditText) dialogSaisieInfosAction.getChampSaisieNombre();
                            } else if (actionParamType instanceof Couleur) {
                                messageDialog = "Sélectionnez la couleur à détecter :";
                                dialogSaisieInfosAction.setValeursListeChoix(Couleur.values());
                                dialogSaisieInfosAction.afficherListeChoix();
                                champDeSaisie = (Spinner) dialogSaisieInfosAction.getChampListeChoix();
                            }

                            // On affiche le message.
                            dialogSaisieInfosAction.setMessage(messageDialog);
                            dialogSaisieInfosAction.afficherBtnNegatif("Annuler");
                            dialogSaisieInfosAction.afficherBtnPositif("Ajouter");

                            dialogSaisieInfosAction.getBtnPositif().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Paramètre qui a été saisi par l'utilisateur.
                                    String parametreDeLAction = champDeSaisie instanceof EditText ? ((EditText) champDeSaisie).getText().toString()
                                            : (champDeSaisie instanceof Spinner ? ((Spinner) champDeSaisie).getSelectedItem().toString() : "");

                                    // On instancie l'étape crée à partir du paramètre saisi par l'utilisateur.
                                    etape = Etape.getEtapeFromCode(tagAction.replace("?", parametreDeLAction));
                                    Log.e(TAG, "etape : " + etape.getCode());

                                    // On ferme le pop-up.
                                    dialogSaisieInfosAction.dismiss();
                                }
                            });

                        } else {
                            // On instancie l'étape créée à partir du tag du bouton cliqué.
                            etape = Etape.getEtapeFromCode(tagAction);
                            Log.e(TAG, "etape : " + etape.getCode());
                        }

                    }
                });
            }
        }
    }
}