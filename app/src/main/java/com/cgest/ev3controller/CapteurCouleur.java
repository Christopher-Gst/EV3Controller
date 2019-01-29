package com.cgest.ev3controller;

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