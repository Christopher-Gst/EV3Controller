package com.cgest.ev3controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cgest.ev3controller.capteur.CapteurCouleur;
import com.cgest.ev3controller.capteur.CapteurProximite;
import com.cgest.ev3controller.capteur.Couleur;
import com.cgest.ev3controller.scenario.Etape;
import com.cgest.ev3controller.scenario.EtapeArreter;
import com.cgest.ev3controller.scenario.EtapeAvancer;
import com.cgest.ev3controller.scenario.EtapeAvancerReculer;
import com.cgest.ev3controller.scenario.EtapeMusique;
import com.cgest.ev3controller.scenario.EtapePause;
import com.cgest.ev3controller.scenario.EtapeReculer;
import com.cgest.ev3controller.scenario.EtapeRotation;
import com.cgest.ev3controller.scenario.Scenario;

import java.util.Collections;

public class EditionScenarioActivity extends AppCompatActivity {

    // TAG de cette activité utilisé pour le Logcat.
    private final static String TAG = "EditionScenarioActivity";

    // Contexte de l'activité.
    private Activity activity;

    // Contrôles utilisateur.
    private TextView textVEditionScenarioTitre;
    private TextView textVActions;
    private TextView textVEditionScenarioNomDuScenario;
    // Boutons circulaire en bas de l'écran.
    private Button btnLancerScenario;

    //private ConstraintLayout constraintLScenario;
    // LinearLayout contenant tous les boutons permettant d'ajouter des actions au scénario.
    private LinearLayout linearLayoutActions;

    // Champ de saisie affichée dans la pop-up lors de l'ajout d'un action au scénario.
    private View champDeSaisie;

    // Etape à ajouter au scénario.
    Etape etape = null;

    // RecyclerView contenant la liste visuelle de étapes du scénario.
    private RecyclerView recyclerVScenario;

    // Adapter de la RecyclerView.
    private RecyclerViewAdapterScenario adapter;

    // Scénario édité ou créée par l'utilisateur.
    Scenario sc;

    // Etape en cours d'édition dans la pop-up.
    Etape etapePopUp = null;

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
        btnLancerScenario = (Button) findViewById(R.id.btnLancerScenario);

        // On affiche la police de caractère de l'application ("Fontdinerdotcom Huggable") sur les Views.
        Utile.appliquerPolicePrincipale(this, textVEditionScenarioTitre);
        Utile.appliquerPolicePrincipale(this, textVActions);
        Utile.appliquerPolicePrincipale(this, textVEditionScenarioNomDuScenario);

        textVEditionScenarioTitre.setText("Mode Manuel");
        textVEditionScenarioNomDuScenario.setText("Scénario de test");

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

                        afficherPopUpEditionEtape(Etape.getEtapeFromCode(tagAction), true);

                    }
                });
            }
        }

        // Initialisation de la RecyclerView.
        initRecyclerView();

        btnLancerScenario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ev3BluetoothManager.envoyerScenario(adapter.scenario);
            }
        });
    }

    public void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerVScenario);
        adapter = new RecyclerViewAdapterScenario(this, recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void afficherPopUpEditionEtape(final Etape etape, final boolean ajout) {

        // On récupère le type du paramètre à spécifier pour l'action.
        Object actionParamType = etape.getParamType();

        // Si l'action a des paramètres à spécifier...
        if (actionParamType != null) {
            // On affiche une boîte de dialogue avec le message et les champs de saisie adaptés, pour paramétrer
            // l'action.
            final CustomDialog dialogSaisieInfosAction = new CustomDialog(activity);
            dialogSaisieInfosAction.setTitre("Édition d'une action");
            // On affiche la pop-up avant même d'avoir spécifié le message, les vues à afficher, ... car la pop-up
            // a besoin de charger ses vues avant d'agir dessus (dans la méthode onCreate()).
            dialogSaisieInfosAction.show();
            String messageDialog = "";

            // On affiche le bon message en fonction du type d'action :
            if (etape instanceof EtapeAvancer || etape instanceof EtapeReculer) {
                if (((EtapeAvancerReculer) etape).getCapteur() == null)
                    messageDialog = "Saisissez la distance de déplacement :";
                else if (((EtapeAvancerReculer) etape).getCapteur() instanceof CapteurProximite)
                    messageDialog = "Saisissez la distance de détection :";
                else if (((EtapeAvancerReculer) etape).getCapteur() instanceof CapteurCouleur)
                    messageDialog = "Sélectionnez la couleur à détecter :";
            } else if (etape instanceof EtapeRotation)
                messageDialog = "Saisissez l'angle de rotation (90°, 180°, ...) :";
            else if (etape instanceof EtapePause)
                messageDialog = "Saisissez la durée de la pause :";

            // Si le paramètre de l'action est un entier, on affiche un EditText.
            if (actionParamType instanceof Integer) {
                dialogSaisieInfosAction.afficherSaisieNombre();
                champDeSaisie = (EditText) dialogSaisieInfosAction.getChampSaisieNombre();
            } // Si le paramètre de l'action est une Couleur, on affiche un Spinner.
            else if (actionParamType instanceof Couleur) {
                messageDialog = "Sélectionnez la couleur à détecter :";
                dialogSaisieInfosAction.setValeursListeChoix(Couleur.values());
                dialogSaisieInfosAction.afficherListeChoix();
                champDeSaisie = (Spinner) dialogSaisieInfosAction.getChampListeChoix();
            }

            // On affiche le message et le texte des boutons.
            dialogSaisieInfosAction.setMessage(messageDialog);
            dialogSaisieInfosAction.afficherBtnNegatif("Annuler");
            dialogSaisieInfosAction.afficherBtnPositif("Valider");

            // On spécifie l'événement à lever quand l'utilisateur appui sur "Valider".
            dialogSaisieInfosAction.getBtnPositif().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // On récupère la saisie de l'utilisateur.
                    String parametreDeLAction = champDeSaisie instanceof EditText ? ((EditText) champDeSaisie).getText().toString()
                            : (champDeSaisie instanceof Spinner ? ((Spinner) champDeSaisie).getSelectedItem().toString() : "");

                    // On extrait le code de l'action passée en paramètre.
                    String[] newCode = etape.getCode().split("\\.");
                    int indexParametreAModifier = -1;

                    // En fonction du type de l'action, on en déduit la partie du code de l'action à modifier.
                    if (etape instanceof EtapeAvancer || etape instanceof EtapeReculer) {
                        if (((EtapeAvancerReculer) etape).getCapteur() == null)
                            indexParametreAModifier = 1;
                        else
                            indexParametreAModifier = 2;
                    } else if (etape instanceof EtapeRotation)
                        indexParametreAModifier = 2;
                    else if (etape instanceof EtapePause)
                        indexParametreAModifier = 1;

                    // On modifie la partie du code concernée.
                    newCode[indexParametreAModifier] = parametreDeLAction;

                    // On reconstitue le code de l'action.
                    String codeAction = "";
                    for (int i = 0; i < newCode.length; i++)
                        codeAction += newCode[i] + (i < (newCode.length - 1) ? "." : "");

                    // On instancie la nouvelle étape avec le nouveau paramètre.
                    etapePopUp = Etape.getEtapeFromCode(codeAction);

                    // On ferme le pop-up.
                    dialogSaisieInfosAction.dismiss();

                    if (ajout) {
                        adapter.scenario.getEtapes().add(etapePopUp);
                        adapter.notifyItemInserted(adapter.scenario.getEtapes().size() - 1);
                    } else {
                        int indexAncienneAction = adapter.scenario.getEtapes().indexOf(etape);
                        adapter.scenario.getEtapes().set(indexAncienneAction, etapePopUp);
                        adapter.notifyItemChanged(indexAncienneAction);
                    }
                }
            });

        } else {
            // Si l'action n'a pas de paramètre, on l'ajoute simplement dans la liste.
            if (ajout) {
                adapter.scenario.getEtapes().add(etape);
                adapter.notifyItemInserted(adapter.scenario.getEtapes().size() - 1);
            }
        }
    }

}