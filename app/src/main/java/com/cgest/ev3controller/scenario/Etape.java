package com.cgest.ev3controller.scenario;

import com.cgest.ev3controller.capteur.Capteur;

public abstract class Etape {

    public abstract String getCode();

    public abstract Object getParamType();

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
            case "M":
                return new EtapeMusique();
            case "ARRET":
                return new EtapeArreter();
            default:
                return null;
        }
    }

    /*
    Permet d'obtenir une instance héritée de Etape, sans se soucier des paramètres.
     */
    public static Etape getEtapeFromCodeNoParam(String monEtapeStr) {
        // On découpe le code de l'étape passée en paramètre avec le ".".
        String[] code = monEtapeStr.split("\\.");
        switch (code[0]) {
            // S'il faut avancer ou reculer...
            case "A":
            case "R":
                boolean avancer = code[0].equals("A");
                // S'il faut avancer pendant une certain durée ou sur une certaine distance...
                if (code[1].charAt(0) == '?') { // S'il manque un nombre (durée ou distance)
                    if (avancer)
                        return new EtapeAvancer();
                    return new EtapeReculer();
                } else { // Sinon, si on avance/recule jusqu'à une détection...
                    Capteur capteur = Capteur.getCapteurFromCodeNoParam(
                            String.valueOf(code[1]) + (code.length >= 3 ? "." + String.valueOf(code[2]) : ""));
                    if (avancer)
                        return new EtapeAvancer(capteur);
                    return new EtapeReculer(capteur);
                }
            case "ROT":
                return new EtapeRotation();
            case "P":
                return new EtapePause();
            case "M":
                return new EtapeMusique();
            case "ARRET":
                return new EtapeArreter();
            default:
                return null;
        }
    }


}