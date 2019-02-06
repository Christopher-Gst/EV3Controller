package com.cgest.ev3controller.capteur;

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
}