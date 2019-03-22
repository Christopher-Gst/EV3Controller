package com.cgest.ev3controller.capteur;

import com.cgest.ev3controller.Utile;

public class CapteurCouleur extends Capteur {

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

    public int getIdImageDescription() {
        return Utile.getIdDrawableAvecNom("icon_capteur_couleur" + Utile.getSuffixeNomImageAction());
    }
}