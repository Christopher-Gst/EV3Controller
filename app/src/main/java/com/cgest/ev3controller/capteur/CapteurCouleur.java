package com.cgest.ev3controller.capteur;

import com.cgest.ev3controller.Utile;
import com.cgest.ev3controller.scenario.EtapeAvancerReculer;

public class CapteurCouleur extends Capteur implements Cloneable {

    private Couleur couleur;
    public final static Couleur COULEUR_PAR_DEFAUT = Couleur.NOIR;

    public CapteurCouleur(Couleur couleur) {
        this.couleur = couleur;
    }

    public CapteurCouleur() {
        this.couleur = COULEUR_PAR_DEFAUT;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    @Override
    public String getCode() {
        return "C." + couleur;
    }

    public void setCouleur(Couleur couleur) {
        this.couleur = couleur;
    }

    @Override
    public Couleur getParamType() {
        return Couleur.BLANC;
    }

    @Override
    public String getNomImageDescription() {
        return "icon_capteur_couleur" + super.getNomImageDescription();
    }

    @Override
    public Object clone() {
        CapteurCouleur clone = null;
        // On récupère l'instance à renvoyer par l'appel de la méthode super.clone()
        clone = (CapteurCouleur) super.clone();
        clone.setCouleur(couleur);
        // on renvoie le clone
        return clone;
    }

}