package com.cgest.ev3controller;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
    // Vues affichant le titre et le message de la pop-up.
    private TextView textVDialogTitre;
    private TextView textVDialogMessage;
    // Views optionnelles à afficher en fonction du contexte d'utilisation de la pop-up.
    private EditText editTDialogSaisie;
    private Spinner spinnerDialogChoix;
    private ImageView imgVDialogImage;
    // Boutons avec des images en bas à gauche.
    private Button btnDialogIcone1;
    private Button btnDialogIcone2;

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

    public void afficherBtnIconeGauche(Drawable image) {
        btnDialogIcone1.setBackground(image);
        btnDialogIcone1.setVisibility(View.VISIBLE);
    }

    public void afficherBtnIconeDroite(Drawable image) {
        btnDialogIcone2.setBackground(image);
        btnDialogIcone2.setVisibility(View.VISIBLE);
    }

    public void afficherSaisieNombre() {
        // On met la taille de l'EditText à 100dp.
        //   - on récupère l'échelle de l'écran.
        ViewGroup.LayoutParams param = editTDialogSaisie.getLayoutParams();
        final float scale = getContext().getResources().getDisplayMetrics().density;
        //   - on calcule combien font 100dp en pixels.
        param.width = (int) (100 * scale + 0.5f);
        editTDialogSaisie.setLayoutParams(param);
        // On change le format du texte pouvant être saisi à Texte.
        editTDialogSaisie.setInputType(InputType.TYPE_CLASS_NUMBER);
        // On rend visible le champ de saisie.
        editTDialogSaisie.setVisibility(View.VISIBLE);
    }

    public void afficherSaisieString() {
        // On met la taille de l'EditText à 500dp.
        //   - on récupère l'échelle de l'écran.
        ViewGroup.LayoutParams param = editTDialogSaisie.getLayoutParams();
        final float scale = getContext().getResources().getDisplayMetrics().density;
        //   - on calcule combien font 500dp en pixels si l'appareil est une tablettre, sinon 250dp.
        param.width = (int) ((Utile.isTablet(activity) ? 500 : 250) * scale + 0.5f);
        editTDialogSaisie.setLayoutParams(param);
        // On change le format du texte pouvant être saisi à Texte.
        editTDialogSaisie.setInputType(InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
        // On rend visible le champ de saisie.
        editTDialogSaisie.setVisibility(View.VISIBLE);
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

        // On affiche le titre et le message.
        textVDialogTitre = (TextView) findViewById(R.id.textVDialogTitre);
        textVDialogMessage = (TextView) findViewById(R.id.textVDialogMessage);
        textVDialogTitre.setText(titre);
        textVDialogMessage.setText(message);

        // On récupère les boutons positif, négatif et optionnel, et on les rend les événements de clic actifs.
        btnPositif = (Button) findViewById(R.id.btnDialogPositif);
        btnNegatif = (Button) findViewById(R.id.btnDialogNegatif);
        btnOptionnel = (Button) findViewById(R.id.btnDialogOptionnel);
        btnPositif.setOnClickListener(this);
        btnNegatif.setOnClickListener(this);
        btnOptionnel.setOnClickListener(this);

        // On récupère les vues optionnelles de la pop-up, à afficher en fonction du contexte d'utilisation de la pop-up.
        editTDialogSaisie = (EditText) findViewById(R.id.editTDialogSaisie);
        spinnerDialogChoix = (Spinner) findViewById(R.id.spinnerDialogChoix);
        imgVDialogImage = (ImageView) findViewById(R.id.imgVDialogImage);

        // On récupère les boutons avec des images de fond en bas à gauche.
        btnDialogIcone1 = (Button) findViewById(R.id.btnDialogIcone1);
        btnDialogIcone2 = (Button) findViewById(R.id.btnDialogIcone2);

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
        textVDialogTitre.setText(titre);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        textVDialogMessage.setText(message);
    }

    public TextView getTextVDialogTitre() {
        return textVDialogTitre;
    }

    public Button getBtnPositif() {
        return btnPositif;
    }

    public Button getBtnNegatif() {
        return btnNegatif;
    }

    public Button getBtnOptionnel() {
        return btnOptionnel;
    }

    public Button getBtnIconGauche() {
        return btnDialogIcone1;
    }

    public Button getBtnIconDroite() {
        return btnDialogIcone2;
    }

    public EditText getChampSaisie() {
        return editTDialogSaisie;
    }

    public Spinner getChampListeChoix() {
        return spinnerDialogChoix;
    }

    public ImageView getImgVDialogImage() {
        return imgVDialogImage;
    }

    public TextView getTextVDialogMessage() {
        return textVDialogMessage;
    }
}