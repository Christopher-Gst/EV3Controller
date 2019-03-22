package com.cgest.ev3controller;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cgest.ev3controller.capteur.CapteurCouleur;
import com.cgest.ev3controller.capteur.CapteurProximite;
import com.cgest.ev3controller.capteur.Couleur;
import com.cgest.ev3controller.scenario.Etape;
import com.cgest.ev3controller.scenario.EtapeAvancer;
import com.cgest.ev3controller.scenario.EtapeAvancerReculer;
import com.cgest.ev3controller.scenario.EtapeParametrable;
import com.cgest.ev3controller.scenario.EtapePause;
import com.cgest.ev3controller.scenario.EtapeReculer;
import com.cgest.ev3controller.scenario.EtapeRotation;
import com.cgest.ev3controller.scenario.Scenario;
import com.cgest.ev3controller.scenario.ScenarioManager;

import java.io.IOException;

import static com.cgest.ev3controller.Utile.isTablet;

public class EditionScenarioActivity extends AppCompatActivity {

    // TAG de cette activité utilisé pour le Logcat.
    private final static String TAG = "EditionScenarioActivity";

    // Contexte de l'activité.
    private Activity activity;

    // Nom du mode sélectionné (manuel ou scan).
    private String mode;

    // Contrôles utilisateur.
    private TextView textVEditionScenarioTitre;
    private TextView textVActions;
    private TextView textVEditionScenarioNomDuScenario;
    private LinearLayout linearLPanneauActions;
    private ImageView imageVSeparationActionsScenario;
    private ImageView imageVScannerUneAction;
    // Boutons circulaire en bas de l'écran.
    private Button btnLancerScenario;
    private Button btnEnregistrerScenario;
    private Button btnEditionScenarioOuvrirUnScenario;

    // LinearLayout contenant tous les boutons permettant d'ajouter des actions au scénario.
    private LinearLayout linearLayoutActions;

    // Champ de saisie affichée dans la pop-up lors de l'ajout d'un action au scénario.
    private View champDeSaisie;

    // Adapter de la RecyclerView.
    private RecyclerViewAdapterScenario adapter;

    // Indique si des modifications n'ont pas été enregistrées.
    boolean modifsNonEnregistrees = false;

    // Nom d'un nouveau scénario par défaut.
    private final static String NOM_NOUVEAU_SCENARIO = "Nom du scénario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edition_scenario);

        // On récupère l'Activity actuel (car elle est passée en paramètre de certaines méthodes).
        activity = this;

        // On récupère le mode sélectionné depuis l'écran d'accueil.
        mode = getIntent().getExtras().getString("MODE");

        // On récupère les vues communes aux modes manuel et scan.
        textVEditionScenarioTitre = (TextView) findViewById(R.id.textVEditionScenarioTitre);
        textVEditionScenarioNomDuScenario = (TextView) findViewById(R.id.textVEditionScenarioNomDuScenario);
        // On affiche la police de caractère de l'application ("Fontdinerdotcom Huggable") sur les Views.
        Utile.appliquerPolicePrincipale(this, textVEditionScenarioTitre);
        Utile.appliquerPolicePrincipale(this, textVEditionScenarioNomDuScenario);

        // Boutons circulaires en bas de l'interface.
        btnLancerScenario = (Button) findViewById(R.id.btnLancerScenario);
        btnEnregistrerScenario = (Button) findViewById(R.id.btnEnregistrerScenario);
        btnEditionScenarioOuvrirUnScenario = (Button) findViewById(R.id.btnEditionScenarioOuvrirUnScenario);

        // On affecte les vues restantes en fonction du mode(manuel ou scan).
        if (mode.equals("manuel")) {
            // On récupère les vues.
            linearLayoutActions = (LinearLayout) findViewById(R.id.linearLayoutActions);
            linearLPanneauActions = (LinearLayout) findViewById(R.id.linearLPanneauActions);
            imageVSeparationActionsScenario = (ImageView) findViewById(R.id.imageVSeparationActionsScenario);
            textVActions = (TextView) findViewById(R.id.textVActions);
            // On affiche la police de caractère de l'application ("Fontdinerdotcom Huggable") sur les Views.
            Utile.appliquerPolicePrincipale(this, textVActions);
            // On rend visible toutes ces vues.
            textVEditionScenarioTitre.setText("Mode Manuel");
            linearLPanneauActions.setVisibility(View.VISIBLE);
            imageVSeparationActionsScenario.setVisibility(View.VISIBLE);

            /* On charge le Linear Layout "linearLayoutActions" avec des boutons correspondant à tous
            les types d'actions utilisables.
             */
            afficherBoutonsActions();

        } else if (mode.equals("scan")) {
            // On récupère les vues.
            imageVScannerUneAction = (ImageView) findViewById(R.id.imageVScannerUneAction);
            // On rend visible le boutons de scan.
            textVEditionScenarioTitre.setText("Mode Scan");
            imageVScannerUneAction.setVisibility(View.VISIBLE);

            // On gère l'appui du bouton de scan de QR code.
            imageVScannerUneAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!Utile.scannerQrCodeInstalle(activity)) {
                        // On crée une pop-up.
                        final CustomDialog dialogTelechargerScanner = new CustomDialog(activity, "Opération impossible", getResources().getString(R.string.mode_scan_indisponible));
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
                        // Si l'application est installée, on l'affiche en mode QR Code.
                        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                        // On demande à l'application le résultat obtenu du scan.
                        startActivityForResult(intent, 0);
                    }
                }
            });
        }

        textVEditionScenarioNomDuScenario.setText(NOM_NOUVEAU_SCENARIO);

        // Initialisation de la RecyclerView.
        initRecyclerView();

        btnLancerScenario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Si le scénario n'est pas vide...
                if (adapter.scenario.getEtapes().size() > 0)
                    Ev3BluetoothManager.envoyerScenario(adapter.scenario);
                else {
                    // Si le scénario est vide, on affiche une erreur.
                    afficherErreurScenarioVide();
                }
            }
        });

        btnEnregistrerScenario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enregistrerScenario(false);
            }
        });

        btnEditionScenarioOuvrirUnScenario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, String.valueOf(modifsNonEnregistrees));

                // Si le scénario actuel contient des actions et que l'utilisateur a fait des modifications qu'il n'a pas enregistré...
                if (modifsNonEnregistrees) {
                    // On informe l'utilisateur qu'il n'a pas enregistré le scénario.
                    final CustomDialog customDemandeEnregistrement = new CustomDialog(activity, "Enregistrer ?", "Le scénario a subi des modifications. L'enregistrer ?");
                    customDemandeEnregistrement.show();
                    customDemandeEnregistrement.afficherBtnNegatif("Non");
                    customDemandeEnregistrement.afficherBtnPositif("Oui");
                    // On gère l'appui sur le bouton "Oui".
                    customDemandeEnregistrement.getBtnPositif().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // On ferme le pop-up.
                            customDemandeEnregistrement.dismiss();
                            // On enregistre le scénario ou non, et on affiche un pop-up pour ouvrir un scénario.
                            // - pour se faire, on affiche le pop-up d'ouverture puis le pop-up d'enregistrement par dessus.
                            // Cela fait que l'utilisateur voit le pop-up d'enregistrement avant le pop-up d'ouverture.
                            ouvrirScenario();
                            enregistrerScenario(false);
                        }
                    });
                    customDemandeEnregistrement.getBtnNegatif().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // On ferme le pop-up.
                            customDemandeEnregistrement.dismiss();
                            ouvrirScenario();
                        }
                    });
                } else {
                    ouvrirScenario();
                }
            }
        });

    }

    private void afficherBoutonsActions() {
        // Si cela n'a pas déjà été fait, on termine la nature de l'appareil (tablette ou smartphone).
        // Cela est nécessaire pour récupérer les bonnes images des types d'actions.
        isTablet(activity);
        // On spécifie l'attribut "context" de Utile pour pouvoir accéder aux ressources de l'application depuis Utile, pour
        // charger les images des actions.
        Utile.setContext(activity);

        // Pour chaque type d'action...
        for (final Etape etape : Scenario.TYPES_ACTIONS_UTILISABLES) {
            // On créé un FameLayout représentant l'action, parent du bouton.
            // On lui donne une apparence de bouton.
            FrameLayout frameLayoutAction = new FrameLayout(activity, null, R.attr.buttonStyle);

            // On créé un Button contenant le texte et l'image de l'action.
            // Le style du bouton est adapté en fonction du type d'appareil (tablette ou smartphone).
            ContextThemeWrapper newContext = new ContextThemeWrapper(activity, R.style.btnAvecTexteEtImageCentresSmartphones);
            Button buttonAction = new Button(newContext);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    etape.getTexteDescription().equals("") ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 0);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            buttonAction.setBackgroundDrawable(null);
            buttonAction.setLayoutParams(params);
            buttonAction.setClickable(true);

            // On affiche le texte et l'image de l'action.
            buttonAction.setText(etape.getTexteDescription());
            try {
                buttonAction.setCompoundDrawablesWithIntrinsicBounds(0, 0, etape.getIdImageDescription(), 0);
                float scale = getResources().getDisplayMetrics().density;
                // On ajuste la distance de séparation du texte et de l'image.
                int dpAsPixels = etape.getTexteDescription().equals("") ? 0 : ((int) ((Utile.isTablet(activity) ? 10 : 5) * scale + 0.5f));
                buttonAction.setCompoundDrawablePadding(dpAsPixels);
            } catch (android.content.res.Resources.NotFoundException e) {
                e.printStackTrace();
            }

            // On ajoute le Button au FrameLayout.
            frameLayoutAction.addView(buttonAction);

            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params1.setMargins(0, 0, 0, 0);
            params1.gravity = Gravity.CENTER;
            frameLayoutAction.setLayoutParams(params1);

            frameLayoutAction.setForegroundGravity(Gravity.CENTER);
            frameLayoutAction.setPadding(0, 0, 0, 0);

            // On ajoute le FrameLayout au LinearLayout de la liste des actions.
            linearLayoutActions.addView(frameLayoutAction);

            // On spécifie l'événement de click du FrameLayout (affichage d'un pop d'édition de l'action, si elle est paramétrable).
            View.OnClickListener listenerAjoutAction = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    afficherPopUpEditionEtape(etape, true);
                }
            };
            frameLayoutAction.setOnClickListener(listenerAjoutAction);
            buttonAction.setOnClickListener(listenerAjoutAction);

        }
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerVScenario);
        adapter = new RecyclerViewAdapterScenario(this, recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // On applique le Listener des événements de toucher ("Swipe de Delete" et "Drag").
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
        adapter.setTouchHelper(touchHelper);
    }

    protected void afficherPopUpEditionEtape(final Etape etape, final boolean ajout) {

        // Si l'action a des paramètres à spécifier...
        if (etape instanceof EtapeParametrable && ((EtapeParametrable) etape).getParamType() != null) {
            // On récupère le type du paramètre à spécifier pour l'action.
            Object actionParamType = ((EtapeParametrable) etape).getParamType();

            // On affiche une boîte de dialogue avec le message et les champs de saisie adaptés, pour paramétrer
            // l'action.
            final CustomDialog dialogSaisieInfosAction = new CustomDialog(activity);
            // On affiche la pop-up avant même d'avoir spécifié le message, les vues à afficher, ... car la pop-up
            // a besoin de charger ses vues avant d'agir dessus (dans la méthode onCreate()).
            dialogSaisieInfosAction.show();
            dialogSaisieInfosAction.setTitre("Édition d'une action");

            // Si le paramètre de l'action est un entier, on affiche un EditText.
            if (actionParamType instanceof Integer) {
                dialogSaisieInfosAction.afficherSaisieNombre();
                champDeSaisie = (EditText) dialogSaisieInfosAction.getChampSaisie();
            } // Si le paramètre de l'action est une Couleur, on affiche un Spinner.
            else if (actionParamType instanceof Couleur) {
                dialogSaisieInfosAction.setValeursListeChoix(Couleur.values());
                dialogSaisieInfosAction.afficherListeChoix();
                champDeSaisie = (Spinner) dialogSaisieInfosAction.getChampListeChoix();
            }

            // On affiche le message et le texte des boutons.
            dialogSaisieInfosAction.setMessage(((EtapeParametrable) etape).getMessageEdition());
            dialogSaisieInfosAction.afficherBtnNegatif("Annuler");
            dialogSaisieInfosAction.afficherBtnPositif("Valider");

            // On spécifie l'événement à lever quand l'utilisateur appui sur "Valider".
            dialogSaisieInfosAction.getBtnPositif().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // On récupère la saisie de l'utilisateur.
                    String parametreActionStr =
                            champDeSaisie instanceof EditText ? ((EditText) champDeSaisie).getText().toString()
                                    : (champDeSaisie instanceof Spinner ? ((Spinner) champDeSaisie).getSelectedItem().toString() : "");

                    // Si l'utilisateur a saisi quelque chose...
                    if (!parametreActionStr.isEmpty()) {

                        Object parametreAction = null;
                        // On récupère le paramètre saisi sous forme d'objet.
                        if (etape instanceof EtapeAvancerReculer) {
                            if (((EtapeAvancerReculer) etape).getCapteur() == null)
                                parametreAction = Integer.valueOf(parametreActionStr);
                            else if (((EtapeAvancerReculer) etape).getCapteur() instanceof CapteurProximite)
                                parametreAction = new CapteurProximite(Integer.valueOf(parametreActionStr));
                            else if (((EtapeAvancerReculer) etape).getCapteur() instanceof CapteurCouleur)
                                parametreAction = new CapteurCouleur(Couleur.valueOf(parametreActionStr.toUpperCase()));
                        } else if (etape instanceof EtapeRotation || etape instanceof EtapePause)
                            parametreAction = Integer.valueOf(parametreActionStr);

                        // On modifie le paramètre de l'action.
                        ((EtapeParametrable) etape).setParametre(parametreAction);

                        // On ferme le pop-up.
                        dialogSaisieInfosAction.dismiss();

                        if (ajout) {
                            adapter.scenario.getEtapes().add(etape);
                            adapter.notifyItemInserted(adapter.scenario.getEtapes().size() - 1);
                        } else {
                            int indexAncienneAction = adapter.scenario.getEtapes().indexOf(etape);
                            adapter.scenario.getEtapes().set(indexAncienneAction, etape);
                            adapter.notifyItemChanged(indexAncienneAction);
                        }
                        modifsNonEnregistrees = true;
                    }
                }
            });

        } else {
            // Si l'action n'a pas de paramètre, on l'ajoute simplement dans la liste.
            if (ajout) {
                adapter.scenario.getEtapes().add(etape);
                adapter.notifyItemInserted(adapter.scenario.getEtapes().size() - 1);
                modifsNonEnregistrees = true;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                // On récupère le code scanné pour savoir quelle action ajouter au scénario.
                String codeAction = data.getStringExtra("SCAN_RESULT");
                // Si le code est valide...
                if (Etape.isCodeValide(codeAction)) {
                    afficherPopUpEditionEtape(Etape.getEtapeFromCode(codeAction), true);
                } else {
                    CustomDialog dialogCodeInvalide = new CustomDialog(activity, "QR code invalide", "Le QR code que vous avez scanné n'est pas valide.");
                    dialogCodeInvalide.show();
                    dialogCodeInvalide.afficherBtnPositif("Ok");
                }
            }
        }
    }

    private void enregistrerScenario(final boolean quitterLeMode) {
        // On peut enregistrer le scénario s'il a au moins une action.
        if (adapter.scenario.getEtapes().size() > 0) {
            // Si le scénario n'a pas de nom, on en demande un.
            if (adapter.scenario.getNom().equals("")) {
                final String messageDialog = "Donnez un nom au scénario :";
                final CustomDialog dialogSaisieNomScenario = new CustomDialog(activity, "Enregistrement", messageDialog);
                dialogSaisieNomScenario.show();
                // On afficher les boutons.
                dialogSaisieNomScenario.afficherBtnPositif("Valider");
                dialogSaisieNomScenario.afficherBtnNegatif("Annuler");
                // On affiche le champ de saisie (EditText).
                dialogSaisieNomScenario.afficherSaisieString();

                // Si l'utilisateur appuie sur "Valider"...
                dialogSaisieNomScenario.getBtnPositif().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Si le nom est déjà utilisé pour un autre scénario...
                        String nomSaisi = dialogSaisieNomScenario.getChampSaisie().getText().toString();
                        if (ScenarioManager.verifierExistenceScenario(activity, nomSaisi)) {
                            dialogSaisieNomScenario.setMessage(messageDialog + "\nCe nom est déjà utilisé. Essayez avec un nom différent.");
                        } else {
                            // Si le nom est libre, on l'applique au scénario.
                            adapter.scenario.setNom(nomSaisi);
                            // On affiche le nom dans l'interface.
                            textVEditionScenarioNomDuScenario.setText(nomSaisi);
                            // On ferme le pop-up.
                            dialogSaisieNomScenario.dismiss();
                            // On enregistre le scénario dans les préférences partagées.
                            ScenarioManager.enregistrerScenario(activity, adapter.scenario);
                            modifsNonEnregistrees = false;
                            if (quitterLeMode) afficherEcranChoixMode();
                        }
                    }
                });
            } else {
                // On enregistre le scénario dans les préférences partagées.
                ScenarioManager.enregistrerScenario(activity, adapter.scenario);
                modifsNonEnregistrees = false;
                if (quitterLeMode) afficherEcranChoixMode();
            }
        } else {
            // Si le scénario est vide, on affiche une erreur.
            afficherErreurScenarioVide();
        }

    }

    private void ouvrirScenario() {
        final CustomDialog dialogOuvrir = new CustomDialog(activity, "Ouvrir un scénario", "Sélectionnez un scénario :");
        dialogOuvrir.show();
        dialogOuvrir.afficherBtnNegatif("Annuler");
        dialogOuvrir.afficherBtnOptionnel("Nouveau");
        dialogOuvrir.afficherBtnIconeGauche(getResources().getDrawable(R.drawable.icon_supprimer_80));
        dialogOuvrir.afficherBtnIconeDroite(getResources().getDrawable(R.drawable.icon_modifier_80));
        // On récupère un tableau contenant les noms des scénarios enregistrés dans l'appareil.
        String[] nomsScenariosEnregistres = ScenarioManager.getTableauNomsScenarios(activity);
        // S'il existe des scénario enregistrés (autres que celui ouvrir actuellement)...
        if (nomsScenariosEnregistres.length > 0) {
            // On affiche la liste des scénarios dans un spinner.
            dialogOuvrir.setValeursListeChoix(nomsScenariosEnregistres);
            dialogOuvrir.afficherListeChoix();
            // On affiche un bouton pour ouvrir le scénario sélectionné.
            dialogOuvrir.afficherBtnPositif("Valider");
        } else {
            // Sinon, on indique qu'il n'existe aucun scénario dans l'appareil.
            dialogOuvrir.setMessage("Aucun scénario n'a encore été créé sur cet appareil.");
            // On cache les boutons "Renommer et "Supprimer".
            dialogOuvrir.getBtnIconGauche().setVisibility(View.GONE);
            dialogOuvrir.getBtnIconDroite().setVisibility(View.GONE);
        }


        // On gère l'appui sur le bouton "Valider".
        dialogOuvrir.getBtnPositif().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On ferme le pop-up.
                dialogOuvrir.dismiss();
                // On récupère le nom du scénario sélectionné.
                String nomScenario = dialogOuvrir.getChampListeChoix().getSelectedItem().toString();
                // On affiche le nom du scénario dans l'interface.
                textVEditionScenarioNomDuScenario.setText(nomScenario);
                // On change l'objet Scenario utilisé par le RecyclerView.
                adapter.setScenario(ScenarioManager.obtenirScenario(activity, nomScenario));
                // On indique qu'aucune modification n'a été faite sur le scénario.
                modifsNonEnregistrees = false;
            }
        });

        // On gère l'appui du nouveau "Nouveau".
        dialogOuvrir.getBtnOptionnel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On ferme le pop-up.
                dialogOuvrir.dismiss();
                // On affiche "Scénario sans nom" dans l'interface.
                textVEditionScenarioNomDuScenario.setText(NOM_NOUVEAU_SCENARIO);
                // On change l'objet Scenario utilisé par le RecyclerView avec un nouveau Scenario.
                adapter.setScenario(new Scenario());
                // On indique qu'aucune modification n'a été faite sur le scénario.
                modifsNonEnregistrees = false;
            }
        });

        // ON GERE LES EVENEMENTS DES BOUTONS SUPPRIMER ET RENOMMER //
        //   -- Bouton de suppression.
        dialogOuvrir.getBtnIconGauche().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On récupère le nom du scénario sélectionné.
                final String nomScenario = dialogOuvrir.getChampListeChoix().getSelectedItem().toString();
                // On affiche une pop-up de confirmation.
                final CustomDialog customConfirmation = new CustomDialog(activity, "Confirmation", "Voulez-vous vraiment supprimer le scénatio \"" + nomScenario + "\" ?");
                customConfirmation.show();
                customConfirmation.afficherBtnPositif("Oui");
                customConfirmation.afficherBtnNegatif("Non");
                // Si l'utilisateur appui sur oui...
                customConfirmation.getBtnPositif().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // On ferme le pop-up.
                        customConfirmation.dismiss();
                        // On supprime le scénario du fichier de préférences.
                        ScenarioManager.supprimerScenario(activity, nomScenario);

                        // On cache le bouton "Valider" car la liste de nom est peut-être vide après la suppression.
                        dialogOuvrir.cacherBtnPositif();
                        // On récupère un tableau contenant les noms des scénarios enregistrés dans l'appareil.
                        String[] nomsScenariosEnregistres = ScenarioManager.getTableauNomsScenarios(activity);
                        // S'il existe des scénario enregistrés (autres que celui ouvrir actuellement)...
                        if (nomsScenariosEnregistres.length > 0) {
                            // On affiche la liste des scénarios dans un spinner.
                            dialogOuvrir.setValeursListeChoix(nomsScenariosEnregistres);
                            dialogOuvrir.afficherListeChoix();
                            // On affiche un bouton pour ouvrir le scénario sélectionné.
                            dialogOuvrir.afficherBtnPositif("Valider");
                        } else {
                            // Sinon, on indique qu'il n'existe aucun scénario dans l'appareil.
                            dialogOuvrir.setMessage("Aucun scénario n'a encore été créé sur cet appareil.");
                            // On cache les boutons "Renommer et "Supprimer".
                            dialogOuvrir.getBtnIconGauche().setVisibility(View.GONE);
                            dialogOuvrir.getBtnIconDroite().setVisibility(View.GONE);
                        }

                        // Si le scénario édité est celui qui doit être supprimé, on créé un nouveau scénario que l'on édite.
                        if (nomScenario.equals(adapter.scenario.getNom())) {
                            // -- On affiche le nom du scénario dans l'interface.
                            textVEditionScenarioNomDuScenario.setText(NOM_NOUVEAU_SCENARIO);
                            // -- On change l'objet Scenario utilisé par le RecyclerView.
                            adapter.setScenario(new Scenario());
                            // -- On indique qu'aucune modification n'a été faite sur le scénario.
                            modifsNonEnregistrees = false;
                        }
                    }
                });
            }
        });

        //   -- Bouton de renommage.
        dialogOuvrir.getBtnIconDroite().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On récupère le nom du scénario sélectionné.
                final String nomScenario = dialogOuvrir.getChampListeChoix().getSelectedItem().toString();
                // On affiche une pop-up pour renommer le scénario.
                final String messageDialog = "Donnez le nouveau nom du scénario \"" + nomScenario + "\" :";
                final CustomDialog dialogSaisieNomScenario = new CustomDialog(activity, "Renommer un scénario", messageDialog);
                dialogSaisieNomScenario.show();
                // On afficher les boutons.
                dialogSaisieNomScenario.afficherBtnPositif("Valider");
                dialogSaisieNomScenario.afficherBtnNegatif("Annuler");
                // On affiche le champ de saisie (EditText).
                dialogSaisieNomScenario.afficherSaisieString();

                // Si l'utilisateur appuie sur "Valider"...
                dialogSaisieNomScenario.getBtnPositif().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Si le nom est déjà utilisé pour un autre scénario...
                        String nomSaisi = dialogSaisieNomScenario.getChampSaisie().getText().toString();
                        if (ScenarioManager.verifierExistenceScenario(activity, nomSaisi) && !nomSaisi.equals(nomScenario)) {
                            dialogSaisieNomScenario.setMessage(messageDialog + "\nCe nom est déjà utilisé. Essayez avec un nom différent.");
                        } else {
                            // On ferme le pop-up.
                            dialogSaisieNomScenario.dismiss();
                            // On renomme le scénario dans les préférences partagées.
                            ScenarioManager.renommerScenario(activity, nomScenario, nomSaisi);
                            // On recharger le spinner pour prendre en compte la modification.
                            dialogOuvrir.setValeursListeChoix(ScenarioManager.getTableauNomsScenarios(activity));
                            // Si le scénario renommé est celui qui est édité actuellement...
                            if (adapter.scenario.getNom().equals(nomScenario)) {
                                // On renomme le scénario dans l'adapter.
                                adapter.scenario.setNom(nomSaisi);
                                // On affiche le nouveau nom dans l'interface.
                                textVEditionScenarioNomDuScenario.setText(nomSaisi);
                            }
                        }
                    }
                });
            }
        });
    }

    private void afficherErreurScenarioVide() {
        // Si le scénario est vide, on affiche une erreur.
        CustomDialog dialogScenarioVide = new CustomDialog(activity, "Opération impossible", "Le scénario est vide. Commencez par ajouter des actions au scénario en " + (mode.equals("manuel") ? "appuyant sur les boutons de la liste de gauche." : "scannant des QR codes. Pour cela, appuyez sur le bouton bleu en bas de l'écran."));
        dialogScenarioVide.show();
        dialogScenarioVide.afficherBtnPositif("Ok");
    }

    @Override
    public void onBackPressed() {
        // Si le scénario actuel contient des actions et que l'utilisateur a fait des modifications qu'il n'a pas enregistré...
        if (modifsNonEnregistrees) {
            // On informe l'utilisateur qu'il n'a pas enregistré le scénario.
            final CustomDialog customDemandeEnregistrement = new CustomDialog(activity, "Enregistrer ?", "Le scénario a subi des modifications. L'enregistrer ?");
            customDemandeEnregistrement.show();
            customDemandeEnregistrement.afficherBtnNegatif("Non");
            customDemandeEnregistrement.afficherBtnPositif("Oui");
            // On gère l'appui sur le bouton "Oui".
            customDemandeEnregistrement.getBtnPositif().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // On ferme le pop-up.
                    customDemandeEnregistrement.dismiss();
                    // On enregistre le scénario ou non, et on revient à l'écran de choix du mode.
                    enregistrerScenario(true);
                }
            });
            customDemandeEnregistrement.getBtnNegatif().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // On ferme le pop-up.
                    customDemandeEnregistrement.dismiss();
                    // On revient à l'écran de choix du mode.
                    afficherEcranChoixMode();
                }
            });
        } else {
            afficherEcranChoixMode();
        }
    }

    private void afficherEcranChoixMode() {
        // On envoie un message au robot signalant qu'on change de mode.
        try {
            Ev3BluetoothManager.sendMessage("EXIT_MODE");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intentChoixMode = new Intent(EditionScenarioActivity.this, ChoixModeActivity.class);
        startActivity(intentChoixMode);
    }

}