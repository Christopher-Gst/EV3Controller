package com.cgest.ev3controller;

public class EtapeReculer extends EtapeAvancerReculer {

    public EtapeReculer(int valeur, int unite) {
        super(valeur, unite);
    }

    public EtapeReculer(int valeur, int unite, Capteur capteur) {
        super(valeur, unite, capteur);
    }
}