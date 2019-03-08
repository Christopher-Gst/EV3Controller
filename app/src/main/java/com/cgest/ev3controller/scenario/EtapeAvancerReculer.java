package com.cgest.ev3controller.scenario;

import com.cgest.ev3controller.capteur.Capteur;

public abstract class EtapeAvancerReculer extends Etape {

    // Temps ou distance de parcours.
    private int valeur;
    // Unité de parcours : secondes ou cm.
    private int unite;
    public static int SECONDES = 0;
    public static int CM = 1;
    public static final int AVANCER = 0;
    public static final int RECULER = 1;

    // Contrainte de parcours : jusqu'à une certaine détection.
    private Capteur capteur;

    // Attributs par défaut en cas de constructeur vide.
    public final static int VALEUR_PAR_DEFAUT = 10;
    private final static int UNITE_PAR_DEFAUT = 0;
    private final static Capteur CAPTEUR_PAR_DEFAUT = null;

    // Soit le robot avance ou recule pendant une certaine durée ou sur une certaine distance.
    public EtapeAvancerReculer(int valeur, int unite) {
        this.valeur = valeur;
        this.unite = unite;
    }

    // Soit le robot avance ou recule jusqu'à une détection.
    public EtapeAvancerReculer(Capteur capteur) {
        this.capteur = capteur;
    }

    public EtapeAvancerReculer() {
        this.valeur = VALEUR_PAR_DEFAUT;
        this.unite = UNITE_PAR_DEFAUT;
        this.capteur = CAPTEUR_PAR_DEFAUT;
    }

    @Override
    public String getCode() {
        return (this instanceof EtapeAvancer ? "A" : "R") + "." + (capteur == null ? (valeur + "." + unite) : capteur.getCode());
    }

    public Capteur getCapteur() {
        return capteur;
    }

    public int getValeur() {
        return valeur;
    }

    public int getUnite() {
        return unite;
    }

    public void setCapteur(Capteur capteur) {
        this.capteur = capteur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

    public void setUnite(int unite) {
        this.unite = unite;
    }

    @Override
    public String toString() {
        return "EtapeAvancerReculer{ sens=" + (this instanceof EtapeAvancer ? "Avancer" : (this instanceof EtapeReculer ? "Reculer" : "?")) +
                ", valeur=" + valeur +
                ", unite=" + unite +
                ", capteur=" + capteur +
                '}';
    }

    @Override
    public Object getParamType() {
        if (capteur != null) return capteur.getParamType();
        return new Integer(0);
    }
}