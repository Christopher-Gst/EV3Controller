package com.cgest.ev3controller.scenario;

import com.cgest.ev3controller.capteur.Capteur;

public class EtapeReculer extends EtapeAvancerReculer {

    public EtapeReculer(int valeur, int unite) {
        super(valeur, unite);
    }

    public EtapeReculer(int valeur, int unite, Capteur capteur) {
        super(valeur, unite, capteur);
    }
}