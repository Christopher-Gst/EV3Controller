package com.cgest.ev3controller.scenario;

import com.cgest.ev3controller.capteur.Capteur;
import com.cgest.ev3controller.capteur.CapteurToucher;

import java.security.InvalidParameterException;

public class EtapeAvancer extends EtapeAvancerReculer {

    public EtapeAvancer(int valeur, int unite) {
        super(valeur, unite);
    }

    public EtapeAvancer(Capteur capteur) {
        super(capteur);
        if (capteur instanceof CapteurToucher)
            throw new InvalidParameterException("Impossible d'utiliser le capteur de toucher en contrainte lorsque le robot avance.");
    }

    public EtapeAvancer() {
        super();
    }

}