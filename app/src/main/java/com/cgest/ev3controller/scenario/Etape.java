package com.cgest.ev3controller.scenario;

import com.cgest.ev3controller.Utile;
import com.cgest.ev3controller.capteur.Capteur;

public abstract class Etape implements Cloneable {

    /**
     * Constructeur par défaut.
     */
    public Etape() {
    }

    // ***** Description textuelle et imagée de l'étape. *****

    /**
     * Permet d'obtenir la description textuelle de l'étape affiché dans les boutons de la liste de gauche du mode manuel.
     *
     * @return
     */
    public abstract String getTexteDescription();

    /**
     * Permet d'obtenir le nom de l'image de l'étape, au format "icon_XXX_[35 ou 45]"
     * (35px pour un affichage sur téléphone et 45px pour un affichage sur tablette).
     * Le nombre de pixels est calculé automatique en fonction du type d'appareil,
     * grâce à l'appel de la méthode getSuffixeNomImageAction() de la classe Utile.
     *
     * @return Le nom de l'image de l'étape, au format "icon_XXX_[35 ou 45]"
     */
    public String getNomImageDescription() {
        return Utile.getSuffixeNomImageAction();
    }

    /**
     * Permet d'obtenir la description textuelle de l'étape, avec plus de détails,
     * affichée dans la liste de droite des modes manuel et scan.
     * Par exemple, avec la méthode getTexteDescription(), on obtiendrait "Avancer",
     * alors qu'avec cette méthode, on obtient "Avancer 20".
     *
     * @return
     */
    public abstract String getTexteAvecDetailsDescription();

    // ***** Gestion du code de l'étape. *****

    /**
     * Permet d'obtenir le code de l'étape (e.g. "A.20.0").
     *
     * @return
     */
    public abstract String getCode();

    /**
     * Permet de savoir si le code d'une étape est valide, autrement dit si un code correspond à une étape pouvant être réellement instanciée.
     *
     * @param code Le code à faire valider.
     * @return True si le code correspond à une étape pouvant être réellement instanciée, sinon False.
     */
    public static boolean isCodeValide(String code) {
        return code.matches(getRegex());
    }

    /**
     * Permet d'obtenir un objet de type Etape à partir de son code.
     *
     * @param monEtapeStr Code de l'étape.
     * @return L'objet Etape correspondant au code.
     */
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
        return "((A|R)\\.[0-9]+\\.(0|1))|((A|R)\\.P+\\.[0-9]+)|((A|R)\\.T)|((A|R)\\.C\\.[a-z]+)|(ROT\\.(0|1|2|3)\\.[0-9]+)|(P\\.[0-9]+)|(B)|(M)";
    }

    /**
     * Permet de créer une nouvelle étape dont les valeurs des attributs extactement les mêmes que ceux de l'instance actuelle.
     *
     * @return
     */
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