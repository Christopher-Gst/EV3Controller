package com.cgest.ev3controller;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.cgest.ev3controller.capteur.Capteur;
import com.cgest.ev3controller.capteur.CapteurCouleur;
import com.cgest.ev3controller.capteur.CapteurProximite;
import com.cgest.ev3controller.capteur.CapteurToucher;
import com.cgest.ev3controller.capteur.Couleur;
import com.cgest.ev3controller.scenario.Etape;
import com.cgest.ev3controller.scenario.EtapeAvancer;
import com.cgest.ev3controller.scenario.EtapeAvancerReculer;
import com.cgest.ev3controller.scenario.EtapeMusique;
import com.cgest.ev3controller.scenario.EtapePause;
import com.cgest.ev3controller.scenario.EtapeReculer;
import com.cgest.ev3controller.scenario.EtapeRotation;
import com.cgest.ev3controller.scenario.Scenario;

import java.util.ArrayList;

public class RecyclerViewAdapterScenarioOld extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Scenario scenario;
    private Activity activity;
    ImageButton btnSupprimerEtape;
    RecyclerView rc;

    private boolean premiereSelectionDeDUneCouleurFaite = false;

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapterScenarioOld(Activity activity, RecyclerView rc) {
        this.activity = activity;
        scenario = new Scenario();
        this.rc = rc;
    }

    // Setter permettant de fournir la liste des messages de la RecyclerView.
    public void setScenario(Scenario scenario) {
        this.scenario.getEtapes().clear();
        this.scenario.getEtapes().addAll(scenario.getEtapes());
        notifyItemRangeInserted(0, scenario.getEtapes().size() - 1);
        colorerLignes();

        Log.e("Liste", "setScenario() fait.");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new ViewHolderEtapeMouvement(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_etape_avancer_reculer, parent, false));
            case 1:
                return new ViewHolderEtapeRotation(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_etape_rotation, parent, false));
            case 2:
                return new ViewHolderEtapePause(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_etape_pause, parent, false));
            case 3:
                return new ViewHolderEtapeMusique(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_etape_musique, parent, false));
            default:
                return null;
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.e("Liste", "onBindViewHolder");

        // On alterne la couleur des lignes.
        colorerLignes();

        Etape etape = scenario.getEtapes().get(position);
        // On affiche les caractéristiques de l'étape sur la view.
        switch (holder.getItemViewType()) {
            case 0:
                ViewHolderEtapeMouvement viewHolder0 = (ViewHolderEtapeMouvement) holder;

                viewHolder0.spinnerEtapeMouvementTypeDeMouvement.setSelection(etape instanceof EtapeAvancer ? 0 : 1);
                // Le robot doit se déplacer sur une certaine distance ou pendant une certaine durée.
                if (((EtapeAvancerReculer) etape).getCapteur() == null) {
                    // On sélection la contrainte "pendant" ou "sur" du spinner.
                    viewHolder0.spinnerEtapeMouvementContrainte.setSelection(((EtapeAvancerReculer) etape).getUnite() == EtapeAvancerReculer.SECONDES ? 0 : 1);
                    // On affiche la durée ou la distance avec la bonne unité.
                    viewHolder0.editTEtapeMouvementValeur.setText(String.valueOf(((EtapeAvancerReculer) etape).getValeur()));
                    viewHolder0.textVEtapeMouvementUnite.setText(((EtapeAvancerReculer) etape).getUnite() == EtapeAvancerReculer.CM ? "cm" : "secondes");
                } else { // Sinon le robot doit se déplacer jusqu'à une détection.
                    // On sélection la contrainte "jusqu'à détection" du spinner.
                    viewHolder0.spinnerEtapeMouvementContrainte.setSelection(2);
                    // On sélection le bon capteur dans le spinner et on remplit les vues spécifiques à chaque capteur.
                    Capteur capteur = ((EtapeAvancerReculer) etape).getCapteur();
                    if (capteur instanceof CapteurCouleur) {
                        viewHolder0.spinnerEtapeMouvementCapteur.setSelection(0);
                        // On remplit le spinner de couleur et on l'affiche.
                        viewHolder0.spinnerEtapeMouvementCouleur.setSelection(((CapteurCouleur) capteur).getCouleur().getIdInEnum());
                    } else if (capteur instanceof CapteurToucher)
                        viewHolder0.spinnerEtapeMouvementCapteur.setSelection(1);
                    else if (capteur instanceof CapteurProximite) {
                        viewHolder0.spinnerEtapeMouvementCapteur.setSelection(etape instanceof EtapeAvancer ? 1 : (etape instanceof EtapeReculer ? 2 : -1));
                        // On remplit le champ de distance et on l'affiche (en plus de "cm").
                        viewHolder0.editTEtapeMouvementDistance.setText(Integer.toString(((CapteurProximite) capteur).getDistanceDetection()));
                    }
                }
                break;

            case 1:
                ViewHolderEtapeRotation viewHolder1 = (ViewHolderEtapeRotation) holder;

                // On affiche la durée de la pause.
                viewHolder1.spinnerEtapeRotationSens.setSelection(((EtapeRotation) etape).getSens() == EtapeRotation.DROITE ? 0 : 1);
                viewHolder1.editTEtapeRotationDegres.setText(Integer.toString(((EtapeRotation) etape).getDegres()));
                break;

            case 2:
                ViewHolderEtapePause viewHolder2 = (ViewHolderEtapePause) holder;

                viewHolder2.editTEtapePauseTemps.setText(Integer.toString(((EtapePause) etape).getDuree()));
                break;

            case 3:
                ViewHolderEtapeMusique viewHolder3 = (ViewHolderEtapeMusique) holder;
                break;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        Log.e("liste", String.valueOf(scenario.getEtapes().size()));
        return scenario.getEtapes().size();
    }

    @Override
    public int getItemViewType(int position) {
        Etape etape = scenario.getEtapes().get(position);
        if (etape instanceof EtapeAvancer || etape instanceof EtapeReculer) {
            return 0;
        } else if (etape instanceof EtapeRotation) {
            return 1;
        } else if (etape instanceof EtapePause) {
            return 2;
        } else if (etape instanceof EtapeMusique) {
            return 3;
        }
        return -1;
    }

    public class ViewHolderEtapeMouvement extends RecyclerView.ViewHolder {

        Spinner spinnerEtapeMouvementTypeDeMouvement;
        Spinner spinnerEtapeMouvementContrainte;
        EditText editTEtapeMouvementValeur;
        TextView textVEtapeMouvementUnite;
        Spinner spinnerEtapeMouvementCapteur;
        Spinner spinnerEtapeMouvementCouleur;
        EditText editTEtapeMouvementDistance;
        TextView textVEtapeMouvementDistance;
        ImageButton btnSupprimerEtape;

        public ViewHolderEtapeMouvement(View itemView) {
            super(itemView);

            spinnerEtapeMouvementTypeDeMouvement = itemView.findViewById(R.id.spinnerEtapeMouvementTypeDeMouvement);
            spinnerEtapeMouvementContrainte = itemView.findViewById(R.id.spinnerEtapeMouvementContrainte);
            editTEtapeMouvementValeur = itemView.findViewById(R.id.editTEtapeMouvementValeur);
            textVEtapeMouvementUnite = itemView.findViewById(R.id.textVEtapeMouvementUnite);
            spinnerEtapeMouvementCapteur = itemView.findViewById(R.id.spinnerEtapeMouvementCapteur);
            spinnerEtapeMouvementCouleur = itemView.findViewById(R.id.spinnerEtapeMouvementCouleur);
            editTEtapeMouvementDistance = itemView.findViewById(R.id.editTEtapeMouvementDistance);
            textVEtapeMouvementDistance = itemView.findViewById(R.id.textVEtapeMouvementDistance);

            editTEtapeMouvementValeur.setImeOptions(EditorInfo.IME_ACTION_DONE);

            // Bouton de suppression d'une étape.
            btnSupprimerEtape = itemView.findViewById(R.id.imageBtnEtapeMouvementSupprimer);
            btnSupprimerEtape.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getPosition();
                    scenario.getEtapes().remove(position);
                    notifyItemRemoved(position);
                    colorerLignes();
                }
            });

            // On remplit le spinner de choix de sens de mouvement avec "Avancer" et "Reculer".
            ArrayList<String> sens = new ArrayList<>();
            sens.add("Avancer");
            sens.add("Reculer");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity,
                    R.layout.spinner_item, sens);
            dataAdapter.setDropDownViewResource(R.layout.spinner_item);
            spinnerEtapeMouvementTypeDeMouvement.setAdapter(dataAdapter);

            // On remplit le spinner de choix de contrainte de déplacement ("pendant", 'sur", "jusqu'à détection").
            ArrayList<String> contraintes = new ArrayList<>();
            contraintes.add("pendant");
            contraintes.add("sur");
            contraintes.add("jusqu'à détection");
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(activity,
                    R.layout.spinner_item, contraintes);
            dataAdapter2.setDropDownViewResource(R.layout.spinner_item);
            spinnerEtapeMouvementContrainte.setAdapter(dataAdapter2);

            remplirSpinnerCapteurs(spinnerEtapeMouvementCapteur, false);

            // On remplit le spinner des couleurs.
            ArrayAdapter<Couleur> dataAdapter4 = new ArrayAdapter<Couleur>(activity,
                    R.layout.spinner_item, Couleur.values());
            dataAdapter4.setDropDownViewResource(R.layout.spinner_item);
            spinnerEtapeMouvementCouleur.setAdapter(dataAdapter4);

            // On contrôle la cohérence de l'interface au fur et à mesure que l'utilisateur manipule les spinners.
            // On modifie aussi le scénario en interne.

            // Sélection d'un sens de déplacement.
            spinnerEtapeMouvementTypeDeMouvement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    EtapeAvancerReculer etape = ((EtapeAvancerReculer) scenario.getEtapes().get(getPosition()));
                    EtapeAvancerReculer newEtape = null;
                    if (i == 0) { // Avancer.
                        newEtape = new EtapeAvancer(etape.getValeur(), etape.getUnite());
                        newEtape.setCapteur(etape.getCapteur());

                        // On cache le capteur de toucher dans le spinner dédié à la sélection du capteur.
                        remplirSpinnerCapteurs(spinnerEtapeMouvementCapteur, false);

                        // Si l'utilisateur sélectionne "Avancer" mais que "Reculer" était sélectionné jusqu'à détection d'un toucher, alors on sélectionne la détection d'une couleur.
                        spinnerEtapeMouvementCapteur.setSelection(0);
                    } else { // Reculer.
                        newEtape = new EtapeReculer(etape.getValeur(), etape.getUnite());
                        newEtape.setCapteur(etape.getCapteur());

                        // On affiche le capteur de toucher dans le spinner dédié à la sélection du capteur.
                        remplirSpinnerCapteurs(spinnerEtapeMouvementCapteur, true);
                    }
                    scenario.getEtapes().set(scenario.getEtapes().indexOf(etape), newEtape);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            // Sélection sur le spinner de contrainte de mouvement.
            spinnerEtapeMouvementContrainte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    // Si l'utilisateur sélectionne "pendant" ou "sur"...
                    if (i == 0 || i == 1) {
                        // On cache tout ce qui concerne les capteurs.
                        hideView(spinnerEtapeMouvementCapteur);
                        hideView(spinnerEtapeMouvementCouleur);
                        hideView(editTEtapeMouvementDistance);
                        hideView(textVEtapeMouvementDistance);
                        // On affiche le champs de valeur et d'unité.
                        showView(textVEtapeMouvementUnite);
                        showView(editTEtapeMouvementValeur);
                        // On affiche la bonne unité.
                        textVEtapeMouvementUnite.setText(i == 0 ? "secondes" : "cm");

                        // On renseigne la bonne unité en mémoire.
                        ((EtapeAvancerReculer) scenario.getEtapes().get(getPosition())).setUnite(i);

                        // On met à null le capteur de l'étape.
                        ((EtapeAvancerReculer) scenario.getEtapes().get(getPosition())).setCapteur(null);
                    } else if (i == 2) { // Sinon, s'il sélectionne "jusqu'à détection"...
                        // Si l'étape n'a pas de capteur de couleur, on en affecte un nouveau.
                        EtapeAvancerReculer etape = (EtapeAvancerReculer) scenario.getEtapes().get(getPosition());
                        if (etape.getCapteur() == null) {
                            etape.setCapteur(new CapteurCouleur());
                            notifyItemChanged(getPosition());
                        }

                        // On affiche le spinner de sélection d'une couleur seulement si le capteur utilisé est celui de couleur.
                        /*if (spinnerEtapeMouvementCapteur.getSelectedItemPosition() == 0)
                            showView(spinnerEtapeMouvementCouleur);
                        else
                            hideView(spinnerEtapeMouvementCouleur);*/
                        // On affiche le spinner de sélection d'un capteur et d'une couleur.
                        showView(spinnerEtapeMouvementCapteur);
                        showView(spinnerEtapeMouvementCouleur);
                        // On cache le champs de valeur et d'unité.
                        hideView(textVEtapeMouvementUnite);
                        hideView(editTEtapeMouvementValeur);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            // Sélection sur le spinner de choix de capteur.
            spinnerEtapeMouvementCapteur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    EtapeAvancerReculer etape = ((EtapeAvancerReculer) scenario.getEtapes().get(getPosition()));
                    Capteur capteur = etape.getCapteur();
                    if (i == 0) { // Détection d'un couleur.
                        // On affiche le spinner de couleur.
                        showView(spinnerEtapeMouvementCouleur);
                        // On cache ce qui concerne les autres capteurs.
                        hideView(editTEtapeMouvementDistance);
                        hideView(textVEtapeMouvementDistance);

                        // On met le capteur de couleur s'il n'existe pas.
                        if (!(capteur instanceof CapteurCouleur)) {
                            etape.setCapteur(new CapteurCouleur());
                            notifyItemChanged(getPosition());
                        }
                    } else if (i == 1 && etape instanceof EtapeReculer) { // Détection du toucher.
                        // On cache ce qui concerne TOUS les capteurs.
                        hideView(spinnerEtapeMouvementCouleur);
                        hideView(editTEtapeMouvementDistance);
                        hideView(textVEtapeMouvementDistance);
                        hideView(spinnerEtapeMouvementCouleur);

                        // On met le capteur de toucher s'il n'existe pas.
                        if (!(capteur instanceof CapteurToucher)) {
                            etape.setCapteur(new CapteurToucher());
                            notifyItemChanged(getPosition());
                        }
                    } else if ((i == 1 && etape instanceof EtapeAvancer) || (i == 2 && etape instanceof EtapeReculer)) {// Détection d'un object à proximité.
                        // On affiche la distance et "cm" par défaut sur 20cm.
                        showView(editTEtapeMouvementDistance);
                        showView(textVEtapeMouvementDistance);
                        // On cache ce qui concerne les autres capteurs.
                        hideView(spinnerEtapeMouvementCouleur);

                        // On met le capteur de détection d'objet à proximité s'il n'existe pas.
                        if (!(capteur instanceof CapteurProximite)) {
                            etape.setCapteur(new CapteurProximite());
                            notifyItemChanged(getPosition());
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            spinnerEtapeMouvementCouleur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Capteur capteur = (((EtapeAvancerReculer) scenario.getEtapes().get(getPosition())).getCapteur());
                    if (capteur instanceof CapteurCouleur)
                        ((CapteurCouleur) capteur).setCouleur(Couleur.values()[i]);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            // On prend en compte la modification des champs (EditText).
            editTEtapeMouvementValeur.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    ((EtapeAvancerReculer) scenario.getEtapes().get(getPosition())).setValeur(charSequence.length() == 0 ? 0 : Integer.valueOf(String.valueOf(charSequence)));
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            editTEtapeMouvementDistance.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    ((CapteurProximite) ((EtapeAvancerReculer) scenario.getEtapes().get(getPosition())).getCapteur()).setDistanceDetection(charSequence.length() == 0 ? 0 : Integer.valueOf(String.valueOf(charSequence)));
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }


    }

    private void remplirSpinnerCapteurs(Spinner spinnerEtapeMouvementCapteur, boolean afficherCapteurToucher) {
        // On remplit le spinner de choix de capteur.
        ArrayList<String> capteurs = new ArrayList<>();
        capteurs.add("d'une couleur");
        if (afficherCapteurToucher)
            capteurs.add("d'un toucher");
        capteurs.add("d'un objet distant de");
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(activity,
                R.layout.spinner_item, capteurs);
        dataAdapter3.setDropDownViewResource(R.layout.spinner_item);
        spinnerEtapeMouvementCapteur.setAdapter(dataAdapter3);
    }

    public class ViewHolderEtapeRotation extends RecyclerView.ViewHolder {

        Spinner spinnerEtapeRotationSens;
        EditText editTEtapeRotationDegres;

        public ViewHolderEtapeRotation(View itemView) {
            super(itemView);

            spinnerEtapeRotationSens = itemView.findViewById(R.id.spinnerEtapeRotationSens);
            editTEtapeRotationDegres = itemView.findViewById(R.id.editTEtapeRotationDegres);

            // Bouton de suppression d'une étape.
            btnSupprimerEtape = itemView.findViewById(R.id.imageBtnEtapeRotationSupprimer);
            btnSupprimerEtape.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getPosition();
                    scenario.getEtapes().remove(position);
                    notifyItemRemoved(position);
                    colorerLignes();
                }
            });

            // On remplit le spinner de choix du sens de rotation ("droite", "gauche").
            ArrayList<String> sens = new ArrayList<>();
            sens.add("droite");
            sens.add("gauche");
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(activity,
                    R.layout.spinner_item, sens);
            dataAdapter2.setDropDownViewResource(R.layout.spinner_item);
            spinnerEtapeRotationSens.setAdapter(dataAdapter2);

            // On prend en compte la modification des champs.
            spinnerEtapeRotationSens.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    ((EtapeRotation) scenario.getEtapes().get(getPosition())).setSens(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            editTEtapeRotationDegres.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    ((EtapeRotation) scenario.getEtapes().get(getPosition())).setDegres(charSequence.length() == 0 ? 0 : Integer.valueOf(String.valueOf(charSequence)));
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

    }

    public class ViewHolderEtapePause extends RecyclerView.ViewHolder {

        EditText editTEtapePauseTemps;

        public ViewHolderEtapePause(View itemView) {
            super(itemView);

            editTEtapePauseTemps = itemView.findViewById(R.id.editTEtapePauseTemps);

            // Bouton de suppression d'une étape.
            btnSupprimerEtape = itemView.findViewById(R.id.imageBtnEtapePauseSupprimer);
            btnSupprimerEtape.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getPosition();
                    scenario.getEtapes().remove(position);
                    notifyItemRemoved(position);
                    colorerLignes();
                }
            });

            editTEtapePauseTemps.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    ((EtapePause) scenario.getEtapes().get(getPosition())).setDuree(charSequence.length() == 0 ? 0 : Integer.valueOf(String.valueOf(charSequence)));
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

    }

    public class ViewHolderEtapeMusique extends RecyclerView.ViewHolder {

        public ViewHolderEtapeMusique(View itemView) {
            super(itemView);

            // Bouton de suppression d'une étape.
            btnSupprimerEtape = itemView.findViewById(R.id.imageBtnEtapeMusiqueSupprimer);
            btnSupprimerEtape.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getPosition();
                    scenario.getEtapes().remove(position);
                    notifyItemRemoved(position);
                    colorerLignes();
                }
            });
        }

    }

    public void colorerLignes() {
        int nbElements = rc.getChildCount();
        for (int i = 0; i < nbElements; i++) {
            RecyclerView.ViewHolder holder = rc.getChildViewHolder(rc.getChildAt(i));
            holder.itemView.setBackgroundColor(i % 2 == 0 ? 000000 : 0x80E0EEEE);
        }
    }

    private void hideView(Spinner view) {
        view.setVisibility(View.GONE);
    }

    private void hideView(TextView view) {
        view.setVisibility(View.GONE);
    }

    private void showView(TextView view) {
        view.setVisibility(View.VISIBLE);
    }

    private void showView(Spinner view) {
        view.setVisibility(View.VISIBLE);
    }

}