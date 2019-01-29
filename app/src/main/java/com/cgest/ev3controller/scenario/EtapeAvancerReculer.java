package com.cgest.ev3controller.scenario;

import com.cgest.ev3controller.capteur.Capteur;

public abstract class EtapeAvancerReculer extends Etape {

    // Temps ou distance de parcours.
    private int valeur;
    // Unité de parcours : secondes ou cm.
    private int unite;
    public static int SECONDES = 0;
    public static int CM = 1;

    // Contrainte de parcours : jusqu'à une certaine détection.
    private Capteur capteur;

    // Soit le robot avance ou recule pendant une certaine durée ou sur une certaine distance.
    public EtapeAvancerReculer(int valeur, int unite) {
        this.valeur = valeur;
        this.unite = unite;
    }

    // Soit le robot avance ou recule jusqu'à une détection.
    public EtapeAvancerReculer(Capteur capteur) {
        this.capteur = capteur;
    }

    @Override
    public String getCode() {
        return (this instanceof EtapeAvancer ? "A" : "R") + "." + (capteur == null ? (valeur + "." + unite) : capteur.getCode());
    }

}