package com.cgest.ev3controller.scenario;

import android.graphics.drawable.Drawable;

import com.cgest.ev3controller.capteur.Capteur;
import com.cgest.ev3controller.capteur.CapteurToucher;

import java.security.InvalidParameterException;

public class EtapeAvancer extends EtapeAvancerReculer {

    public EtapeAvancer(int valeur, int unite) {
        super(valeur, unite);
    }

    public EtapeAvancer(Capteur capteur) {
        super(capteur);
    }

    public EtapeAvancer() {
        super();
    }

    @Override
    public String getTexteAvecDetailsDescription() {
        return "Avancer" + (getCapteur() == null ? " " + getValeur() : "");
    }

    public String getTexteDescription() {
        return "Avancer";
    }

}