package com.cgest.ev3controller.capteur;

public class CapteurCouleur extends Capteur {

    private Couleur couleur;

    public CapteurCouleur(Couleur couleur) {
        this.couleur = couleur;
    }

    @Override
    public String getCode() {
        return "C." + couleur;
    }
}