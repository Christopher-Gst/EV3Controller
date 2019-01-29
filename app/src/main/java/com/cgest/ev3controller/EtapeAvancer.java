package com.cgest.ev3controller;

import java.security.InvalidParameterException;

public class EtapeAvancer extends EtapeAvancerReculer {

    public EtapeAvancer(int valeur, int unite) {
        super(valeur, unite);
    }

    public EtapeAvancer(int valeur, int unite, Capteur capteur) {
        super(valeur, unite, capteur);
        if (capteur instanceof CapteurToucher)
            throw new InvalidParameterException("Impossible d'utiliser le capteur de toucher en contrainte lorsque le robot avance.");
    }

}