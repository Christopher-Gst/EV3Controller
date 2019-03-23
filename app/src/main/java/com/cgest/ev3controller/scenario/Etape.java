package com.cgest.ev3controller.scenario;

import com.cgest.ev3controller.Utile;
import com.cgest.ev3controller.capteur.Capteur;

public abstract class Etape implements Cloneable {

    // Constructeur par defaut.
    public Etape() {
    }

    public abstract String getCode();

    public abstract String getTexteDescription();

    public String getNomImageDescription() {
        return Utile.getSuffixeNomImageAction();
    }

    public abstract String getTexteAvecDetailsDescription();

    public static Etape getEtapeFromCode(String monEtapeStr) {
        // On découpe le code de l'étape passée en paramètre avec le ".".
        String[] code = monEtapeStr.split("\\.");
        switch (code[0]) {
            // S'il faut avancer ou reculer...
            case "A":
            case "R":
                boolean avancer = code[0].equals("A");

                // S'il faut avancer pendant une certain durée ou sur une certaine distance...
                if (Character.isDigit(code[1].charAt(0)) || code[1].charAt(0) == '-') { // Détection d'un chiffre ou d'un
                    // "-" (pour "-1").
                    int valeur = Integer.parseInt(code[1]);
                    int unite = Integer.parseInt(code[2]);
                    if (avancer)
                        return new EtapeAvancer(valeur, unite);
                    return new EtapeReculer(valeur, unite);
                } else { // Sinon, si on avance/recule jusqu'à une détection...
                    Capteur capteur = Capteur.getCapteurFromCode(
                            String.valueOf(code[1]) + (code.length >= 3 ? "." + String.valueOf(code[2]) : ""));
                    if (avancer)
                        return new EtapeAvancer(capteur);
                    return new EtapeReculer(capteur);
                }
            case "ROT":
                return new EtapeRotation(Integer.valueOf(code[1]), Integer.valueOf(code[2]));
            case "P":
                return new EtapePause(Integer.valueOf(code[1]));
            case "B":
                return new EtapeBip();
            case "M":
                return new EtapeMusique();
            case "ARRET":
                return new EtapeArreter();
            default:
                return null;
        }
    }

    public static String getRegex() {
        return "((A|R)\\.[0-9]+\\.(0|1))|((A|R)\\.P+\\.[0-9]+)|((A|R)\\.T)|((A|R)\\.C\\.[a-z]+)|(ROT\\.(0|1)\\.[0-9]+)|(P\\.[0-9]+)|(B)|(M)";
    }

    public static boolean isCodeValide(String code) {
        return code.matches(getRegex());
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