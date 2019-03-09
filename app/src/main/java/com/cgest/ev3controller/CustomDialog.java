package com.cgest.ev3controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cgest.ev3controller.capteur.Couleur;

public class CustomDialog extends Dialog implements
        android.view.View.OnClickListener {

    private Activity activity;
    private String titre;
    private String message;
    // Boutons d'annulation et de validation.
    private Button btnPositif;
    private Button btnNegatif;
    // Bouton optionnel affiché en bas à gauche.
    private Button btnOptionnel;
    // Vue affichant le message de la pop-up.
    private TextView textVDialogMessage;
    // Views optionnelles à afficher en fonction du contexte d'utilisation de la pop-up.
    private EditText editTDialogNombre;
    private Spinner spinnerDialogChoix;

    public CustomDialog(Activity activity) {
        super(activity);
        this.activity = activity;
        this.titre = "";
        this.message = "";
    }

    public CustomDialog(Activity activity, String titre, String message) {
        this(activity);
        this.activity = activity;
        this.titre = titre;
        this.message = message;
    }

    public void cacherBtnPositif() {
        btnPositif.setVisibility(View.GONE);
    }

    public void cacherBtnNegatif() {
        btnNegatif.setVisibility(View.GONE);
    }

    public void cacherBtnOptionnel() {
        btnOptionnel.setVisibility(View.GONE);
    }

    public void afficherBtnPositif(String texte) {
        btnPositif.setText(texte);
        btnPositif.setVisibility(View.VISIBLE);
    }

    public void afficherBtnNegatif(String texte) {
        btnNegatif.setText(texte);
        btnNegatif.setVisibility(View.VISIBLE);
    }

    public void afficherBtnOptionnel(String texte) {
        btnOptionnel.setText(texte);
        btnOptionnel.setVisibility(View.VISIBLE);
    }

    public void afficherSaisieNombre() {
        editTDialogNombre.setVisibility(View.VISIBLE);
    }

    public void afficherListeChoix() {
        spinnerDialogChoix.setVisibility(View.VISIBLE);
    }

    public void setValeursListeChoix(Object[] valeurs) {
        // On remplit le spinner avec le tableau passé en paramètres.
        ArrayAdapter<Object> dataAdapter = new ArrayAdapter<Object>(activity,
                R.layout.spinner_item_new, valeurs);
        spinnerDialogChoix.setAdapter(dataAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog);
        // Le pop-up n'est pas annulable en cliquant sur le parent, c'est-à-dire en dehors.
        setCancelable(false);

        // On récupère les boutons positif, négatif et optionnel, et on les rend les événements de clic actifs.
        btnPositif = (Button) findViewById(R.id.btnDialogPositif);
        btnNegatif = (Button) findViewById(R.id.btnDialogNegatif);
        btnOptionnel = (Button) findViewById(R.id.btnDialogOptionnel);
        btnPositif.setOnClickListener(this);
        btnNegatif.setOnClickListener(this);
        btnOptionnel.setOnClickListener(this);

        // On récupère les vues optionnelles de la pop-up, à afficher en fonction du contexte d'utilisation de la pop-up.
        editTDialogNombre = (EditText) findViewById(R.id.editTDialogNombre);
        spinnerDialogChoix = (Spinner) findViewById(R.id.spinnerDialogChoix);

        // On affiche le titre et le message.
        TextView textVDialogTitre = (TextView) findViewById(R.id.textVDialogTitre);
        textVDialogMessage = (TextView) findViewById(R.id.textVDialogMessage);
        textVDialogTitre.setText(titre);
        textVDialogMessage.setText(message);

        // On applique la police de caractères de l'applications sur le texte du dialog.
        Utile.appliquerPolicePrincipale(activity, textVDialogTitre);
        Utile.appliquerPolicePrincipale(activity, textVDialogMessage);
        Utile.appliquerPolicePrincipale(activity, btnPositif);
        Utile.appliquerPolicePrincipale(activity, btnNegatif);
        Utile.appliquerPolicePrincipale(activity, btnOptionnel);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDialogPositif:
                dismiss();
                break;
            case R.id.btnDialogNegatif:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        textVDialogMessage.setText(message);
    }

    public Button getBtnPositif() {
        return btnPositif;
    }

    public Button getBtnNegatif() {
        return btnNegatif;
    }

    public EditText getChampSaisieNombre() {
        return editTDialogNombre;
    }

    public Spinner getChampListeChoix() {
        return spinnerDialogChoix;
    }
}