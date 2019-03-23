package com.cgest.ev3controller.capteur;

import com.cgest.ev3controller.Utile;
import com.cgest.ev3controller.scenario.EtapeAvancerReculer;

public abstract class Capteur implements Cloneable {

    public abstract String getCode();

    public abstract Object getParamType();

    public String getNomImageDescription() {
        return Utile.getSuffixeNomImageAction();
    }

    public static Capteur getCapteurFromCode(String code) {
        String[] codeTab = code.split("\\.");
        switch (codeTab[0]) {
            case "C":
                return new CapteurCouleur(Couleur.valueOf(codeTab[1].toUpperCase()));
            case "P":
                return new CapteurProximite(Integer.valueOf(codeTab[1]));
            case "T":
                return new CapteurToucher();
            default:
                return null;
        }
    }

    /*
    Permet d'obtenir une instance héritant de Capteur sans se soucier des paramètres de l'Etape considérée.
    Autrement dit, on peut obtenir un objet de type Capteur avec des "?" dans le code de l'étape associée.
     */
    public static Capteur getCapteurFromCodeNoParam(String code) {
        String[] codeTab = code.split("\\.");
        switch (codeTab[0]) {
            case "C":
                return new CapteurCouleur();
            case "P":
                return new CapteurProximite();
            case "T":
                return new CapteurToucher();
            default:
                return null;
        }
    }

    @Override
    public Object clone() {
        Object o = null;
        try {
            // On récupère l'instance à renvoyer par l'appel de la méthode super.clone()
            o = super.clone();
        } catch (CloneNotSupportedException cnse) {
            // Ne devrait jamais arriver car nous implémentons
            // l'interface Cloneable
            cnse.printStackTrace(System.err);
        }
        // on renvoie le clone
        return o;
    }

}