package com.cgest.ev3controller;

import java.security.InvalidParameterException;

public abstract class EtapeAvancerReculer extends Etape {

    // Temps ou distance de parcours.
    private int valeur;
    // Unité de parcours : secondes ou cm.
    private int unite;
    public static int SECONDES = 0;
    public static int CM = 1;

    // Contrainte de parcours : jusqu'à une certaine détection.
    private Capteur capteur;

    public EtapeAvancerReculer(int valeur, int unite) {
        this.valeur = valeur;
        this.unite = unite;
    }

    public EtapeAvancerReculer(int valeur, int unite, Capteur capteur) {
        this.valeur = valeur;
        this.unite = unite;
        this.capteur = capteur;
    }

    @Override
    public String getCode() {
        return (this instanceof EtapeAvancer ? "A" : "R") + "." + valeur + "." + unite + (capteur != null ? "." + capteur.getCode() : "");
    }

}