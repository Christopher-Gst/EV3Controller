package com.cgest.ev3controller;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.cgest.ev3controller.scenario.Etape;
import com.cgest.ev3controller.scenario.Scenario;

import java.util.Collections;

public class RecyclerViewAdapterScenario extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {

    public Scenario scenario;
    private Activity activity;
    RecyclerView rc;
    private ItemTouchHelper touchHelper;
    private static String TAG = "RecyclerViewAdapterScenario";

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapterScenario(Activity activity, RecyclerView rc) {
        this.activity = activity;
        scenario = new Scenario();
        this.rc = rc;
    }

    // Setter permettant de fournir la liste des messages de la RecyclerView.
    public void setScenario(Scenario scenario) {
        // On recopie le scénario passé en paramètres avec la liste des étapes et le nom du scénario.
        this.scenario.getEtapes().clear();
        this.scenario.getEtapes().addAll(scenario.getEtapes());
        this.scenario.setNom(scenario.getNom());
        //notifyItemRangeInserted(0, this.scenario.getEtapes().size() - 1);
        notifyDataSetChanged();
        Log.e("Liste", "setScenario() fait.");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderEtape(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_action, parent, false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.e("Liste", "onBindViewHolder");

        Etape etape = scenario.getEtapes().get(position);
        // On affiche les caractéristiques de l'étape sur la view.
        final ViewHolderEtape viewHolder = (ViewHolderEtape) holder;

        viewHolder.layoutActionTexteEtImage.setText(etape.getDescriptionTextuelle());

        String code = etape.getCode();
        int idImage = 0;
        // Si on avance ou recule pendant une durée...
        if (code.matches("(A|R)\\.[0-9]+\\.0")) {
            idImage = R.drawable.icon_horloge3;
        } // Si on avance ou recule une distance...
        else if (code.matches("(A|R)\\.[0-9]+\\.1")) {
            idImage = R.drawable.icon_regle;
        } // Si on avance / reculer jusqu'à détection d'un objet à proximité...
        else if (code.matches("(A|R)\\.P+\\.[0-9]+")) {
            idImage = R.drawable.icon_capteur_obstacle;
        } // Si on avance / reculer jusqu'à détection d'un toucher...
        else if (code.matches("(A|R)\\.T")) {
            idImage = R.drawable.icon_capteur_toucher;
        } // Si on avance / reculer jusqu'à détection d'une couleur...
        else if (code.matches("(A|R)\\.C\\.[a-z]+")) {
            idImage = R.drawable.icon_capteur_couleur;
        } // Si on fait une rotation à droite...
        else if (code.matches("ROT\\.0\\.[0-9]+")) {
            idImage = R.drawable.icon_droite;
        } // Si on fait une rotation à gauche...
        else if (code.matches("ROT\\.1\\.[0-9]+")) {
            idImage = R.drawable.icon_gauche;
        } // Si on fait une pause...
        else if (code.matches("P\\.[0-9]+")) {
            idImage = R.drawable.icon_sablier;
        } else if (code.equals("B")) {
            idImage = R.drawable.icon_cloche;
        } else if (code.equals("M")) {
            idImage = R.drawable.icon_musique;
        }
        if (idImage != 0)
            viewHolder.layoutActionTexteEtImage.setCompoundDrawablesWithIntrinsicBounds(0, 0, idImage, 0);

        viewHolder.imgVDeplacerAction.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN || event.getActionMasked() == MotionEvent.ACTION_UP) {
                    touchHelper.startDrag(viewHolder);
                }
                return false;
            }

        });
    }

    public void setTouchHelper(ItemTouchHelper touchHelper) {
        this.touchHelper = touchHelper;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        Log.e("liste", String.valueOf(scenario.getEtapes().size()));
        return scenario.getEtapes().size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public class ViewHolderEtape extends RecyclerView.ViewHolder {

        Button layoutActionTexteEtImage;
        ImageView imgVDeplacerAction;

        public ViewHolderEtape(View itemView) {
            super(itemView);
            imgVDeplacerAction = itemView.findViewById(R.id.imgVLayoutActionDeplacer);
            layoutActionTexteEtImage = itemView.findViewById(R.id.layoutActionTexteEtImage);
            Utile.appliquerPolicePrincipale(activity, layoutActionTexteEtImage);

            // On gère le clic d'une action pour afficher la pop-up de modification.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG, scenario.getEtapes().get(getPosition()).getCode());

                    final Etape etape = scenario.getEtapes().get(getPosition());

                    // Si l'action possède des paramètres modifiables...
                    if (etape.getParamType() != null) {
                        // On affiche le pop-up de modification de l'action.
                        ((EditionScenarioActivity) activity).afficherPopUpEditionEtape(etape, false);
                    }
                }
            });
        }

    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        // On intervertie les deux actions.
        Collections.swap(scenario.getEtapes(), fromPosition, toPosition);
        // On notifie le changement de position à l'adapter.
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        // On supprime l'action du scénario.
        scenario.getEtapes().remove(position);
        // On notifie la suppression d'action à l'adapter.
        notifyItemRemoved(position);
    }

}
