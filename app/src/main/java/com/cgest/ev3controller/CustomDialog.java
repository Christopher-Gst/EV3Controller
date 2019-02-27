package com.cgest.ev3controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class CustomDialog extends Dialog implements
        android.view.View.OnClickListener {

    private Activity activity;
    private String titre;
    private String message;
    private Button btnPositif;
    private Button btnNegatif;

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

    public void afficherBtnPositif(String texte) {
        btnPositif.setText(texte);
        btnPositif.setVisibility(View.VISIBLE);
    }

    public void afficherBtnNegatif(String texte) {
        btnNegatif.setText(texte);
        btnNegatif.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog);
        // On récupère les boutons positif et négatif et on les rend les événements de clic actifs.
        btnPositif = (Button) findViewById(R.id.btnDialogPositif);
        btnNegatif = (Button) findViewById(R.id.btnDialogNegatif);
        btnPositif.setOnClickListener(this);
        btnNegatif.setOnClickListener(this);
        // On affiche le titre et le message.
        TextView textVDialogTitre = (TextView) findViewById(R.id.textVDialogTitre);
        TextView textVDialogMessage = (TextView) findViewById(R.id.textVDialogMessage);
        textVDialogTitre.setText(titre);
        textVDialogMessage.setText(message);
        // On applique la police de caractères de l'applications sur le texte du dialog.
        Utile.appliquerPolicePrincipale(activity, textVDialogTitre);
        Utile.appliquerPolicePrincipale(activity, textVDialogMessage);
        Utile.appliquerPolicePrincipale(activity, btnPositif);
        Utile.appliquerPolicePrincipale(activity, btnNegatif);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDialogPositif:
                activity.finish();
                break;
            case R.id.btnDialogNegatif:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}