package com.cgest.ev3controller.scenario;

import com.cgest.ev3controller.capteur.Capteur;

public class EtapeReculer extends EtapeAvancerReculer {

    public EtapeReculer(int valeur, int unite) {
        super(valeur, unite);
    }

    public EtapeReculer(Capteur capteur) {
        super(capteur);
    }

    public EtapeReculer() {
        super();
    }

    @Override
    public String getDescriptionTextuelle() {
        return "Reculer" + (getCapteur() == null ? " " + getValeur() : "");
    }

}