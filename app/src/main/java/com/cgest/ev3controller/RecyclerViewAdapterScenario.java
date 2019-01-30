package com.cgest.ev3controller;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.cgest.ev3controller.scenario.Scenario;

import java.util.ArrayList;

public class RecyclerViewAdapterScenario extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Scenario scenario;
    private Activity activity;

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapterScenario(Activity activity) {
        this.activity = activity;
        scenario = new Scenario();
    }

    // Setter permettant de fournir la liste des messages de la RecyclerView.
    public void setScenario(Scenario scenario) {
        this.scenario.getEtapes().clear();
        this.scenario.getEtapes().addAll(scenario.getEtapes());
        notifyItemRangeInserted(0, scenario.getEtapes().size() - 1);

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

        Etape etape = scenario.getEtapes().get(position);
        // On affiche les caractéristiques de l'étape sur la view.
        switch (holder.getItemViewType()) {
            case 0:
                ViewHolderEtapeMouvement viewHolder0 = (ViewHolderEtapeMouvement) holder;
                viewHolder0.spinnerEtapeMouvementTypeDeMouvement.setSelection(etape instanceof EtapeAvancer ? 0 : 1);
                // Le robot doit se déplacer sur une certaine distance ou pendant une certaine durée.
                if (((EtapeAvancerReculer) etape).getCapteur() != null) {
                    // On sélection la contrainte "pendant" ou "sur" du spinner.
                    viewHolder0.spinnerEtapeMouvementContrainte.setSelection(((EtapeAvancerReculer) etape).getUnite() == EtapeAvancerReculer.SECONDES ? 0 : 1);
                    // On cache tout ce qui concerne les capteurs.
                    viewHolder0.spinnerEtapeMouvementCapteur.setVisibility(View.INVISIBLE);
                    viewHolder0.spinnerEtapeMouvementCouleur.setVisibility(View.INVISIBLE);
                    viewHolder0.editTEtapeMouvementDistance.setVisibility(View.INVISIBLE);
                    viewHolder0.textVEtapeMouvementDistance.setVisibility(View.INVISIBLE);
                    // On affiche la durée ou la distance avec la bonne unité.
                    viewHolder0.editTEtapeMouvementValeur.setVisibility(View.VISIBLE);
                    viewHolder0.textVEtapeMouvementUnite.setVisibility(View.VISIBLE);
                    viewHolder0.editTEtapeMouvementValeur.setText(((EtapeAvancerReculer) etape).getValeur());
                    viewHolder0.textVEtapeMouvementUnite.setText(((EtapeAvancerReculer) etape).getUnite() == EtapeAvancerReculer.CM ? "cm" : "secondes");
                } else { // Sinon le robot doit se déplacer jusqu'à une détection.
                    // On sélection la contrainte "jusqu'à détection" du spinner.
                    viewHolder0.spinnerEtapeMouvementContrainte.setSelection(2);
                    // On cache ce qui concerne une distance ou une durée.
                    viewHolder0.editTEtapeMouvementValeur.setVisibility(View.INVISIBLE);
                    viewHolder0.textVEtapeMouvementUnite.setVisibility(View.INVISIBLE);
                    // On affiche tout ce qui concerne les capteurs.
                    // D'abord, on cache tous les champs de capteur (ex : distance, couleurs, ...)
                    viewHolder0.spinnerEtapeMouvementCouleur.setVisibility(View.INVISIBLE);
                    viewHolder0.textVEtapeMouvementDistance.setVisibility(View.INVISIBLE);
                    viewHolder0.editTEtapeMouvementDistance.setVisibility(View.INVISIBLE);
                    // On sélection le bon capteur dans le spinner et on remplit les vues spécifiques à chaque capteur.
                    Capteur capteur = ((EtapeAvancerReculer) etape).getCapteur();
                    if (capteur instanceof CapteurCouleur) {
                        viewHolder0.spinnerEtapeMouvementCapteur.setSelection(0);
                        // On remplit le spinner de couleur et on l'affiche.
                        viewHolder0.spinnerEtapeMouvementCouleur.setSelection(((CapteurCouleur) capteur).getCouleur().getIdInEnum());
                        viewHolder0.spinnerEtapeMouvementCouleur.setVisibility(View.VISIBLE);
                    } else if (capteur instanceof CapteurToucher)
                        viewHolder0.spinnerEtapeMouvementCapteur.setSelection(1);
                    else if (capteur instanceof CapteurProximite) {
                        viewHolder0.spinnerEtapeMouvementCapteur.setSelection(2);
                        // On remplit le champ de distance et on l'affiche (en plus de "cm").
                        viewHolder0.editTEtapeMouvementDistance.setText(((CapteurProximite) capteur).getDistanceDetection());
                        viewHolder0.editTEtapeMouvementDistance.setVisibility(View.VISIBLE);
                        viewHolder0.textVEtapeMouvementDistance.setVisibility(View.VISIBLE);
                    }
                }
                break;

            case 2:
                ViewHolderEtapeRotation viewHolder2 = (ViewHolderEtapeRotation) holder;
                break;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        Log.e("liste", String.valueOf(scenario.getEtapes().size()));
        return scenario.getEtapes().size();
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

            // On remplit le spinner de choix de sens de mouvement avec "Avancer" et "Reculer".
            ArrayList<String> sens = new ArrayList<>();
            sens.add("Avancer");
            sens.add("Reculer");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity,
                    android.R.layout.simple_spinner_item, sens);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEtapeMouvementTypeDeMouvement.setAdapter(dataAdapter);

            // On remplit le spinner de choix de contrainte de déplacement ("pendant", 'sur", "jusqu'à détection").
            ArrayList<String> contraintes = new ArrayList<>();
            sens.add("pendant");
            sens.add("sur");
            sens.add("jusqu'à détection");
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(activity,
                    android.R.layout.simple_spinner_item, contraintes);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEtapeMouvementContrainte.setAdapter(dataAdapter2);

            // On remplit le spinner de choix de capteur.
            ArrayList<String> capteurs = new ArrayList<>();
            sens.add("d'une couleur");
            sens.add("d'un toucher");
            sens.add("d'un objet distant de");
            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(activity,
                    android.R.layout.simple_spinner_item, capteurs);
            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEtapeMouvementCapteur.setAdapter(dataAdapter3);

            // On remplit le spinner des couleurs.
            ArrayAdapter<Couleur> dataAdapter4 = new ArrayAdapter<Couleur>(activity,
                    android.R.layout.simple_spinner_item, Couleur.values());
            dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEtapeMouvementCapteur.setAdapter(dataAdapter4);
        }

    }

    public class ViewHolderEtapeRotation extends RecyclerView.ViewHolder {

        Spinner spinnerEtapeRotationSens;
        EditText editTEtapeRotationDegres;

        public ViewHolderEtapeRotation(View itemView) {
            super(itemView);

            spinnerEtapeRotationSens = itemView.findViewById(R.id.spinnerEtapeRotationSens);
            editTEtapeRotationDegres = itemView.findViewById(R.id.editTEtapeRotationDegres);
        }

    }

    public class ViewHolderEtapePause extends RecyclerView.ViewHolder {

        EditText editTEtapePauseTemps;

        public ViewHolderEtapePause(View itemView) {
            super(itemView);

            editTEtapePauseTemps = itemView.findViewById(R.id.editTEtapePauseTemps);
        }

    }

    public class ViewHolderEtapeMusique extends RecyclerView.ViewHolder {

        public ViewHolderEtapeMusique(View itemView) {
            super(itemView);
        }

    }


}