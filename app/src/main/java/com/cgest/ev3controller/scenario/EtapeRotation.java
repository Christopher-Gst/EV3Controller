package com.cgest.ev3controller.scenario;

import com.cgest.ev3controller.Utile;

public class EtapeRotation extends Etape implements EtapeParametrable {

    // Rotation à droite ou à gauche repérée par des constantes.
    private int sens;
    public final static int DROITE = 0;
    public final static int GAUCHE = 1;
    //public final static int BAISSER = 2;
    //public final static int MONTER = 3;
    // Degré de rotation à effectuer.
    private int degres;
    // Valeurs par défaut pour le constructeur vide.
    private final static int SENS_PAR_DEFAUT = DROITE;
    private final static int DEGRES_PAR_DEFAUT = 90;

    public EtapeRotation(int sens, int degres) {
        this.sens = sens;
        this.degres = degres;
    }

    public EtapeRotation() {
        this.sens = SENS_PAR_DEFAUT;
        this.degres = DEGRES_PAR_DEFAUT;
    }

    @Override
    public String getCode() {
        return "ROT." + sens + "." + degres;
    }

    public int getSens() {
        return sens;
    }

    public int getDegres() {
        return degres;
    }

    public void setSens(int sens) {
        this.sens = sens;
    }

    public void setDegres(int degres) {
        this.degres = degres;
    }

    @Override
    public Integer getParamType() {
        return new Integer(0);
    }

    @Override
    public String getTexteAvecDetailsDescription() {
        return getTexteDescription();
    }

    public String getTexteDescription() {
        return "Tourner";
    }

    /*
    public String getTexteDescription() {
        switch (sens) {
            case DROITE:
            case GAUCHE:
                return "Tourner";
            case MONTER:
                return "Lâcher";
            case BAISSER:
                return "Saisir";
            default:
                return "";
        }
    }
    */

    public String getNomImageDescription() {
        return "icon_" + (sens == DROITE ? "droite" : "gauche") + super.getNomImageDescription();
    }

    /*
    public String getNomImageDescription() {
        String debutNom = "";
        switch (sens) {
            case DROITE:
                debutNom = "droite";
                break;
            case GAUCHE:
                debutNom = "gauche";
                break;
            case MONTER:
            case BAISSER:
                debutNom = "bras";
                break;
        }
        return "icon_" + debutNom + super.getNomImageDescription();
    }
    */

    @Override
    public String getMessageEdition() {
        return "Saisissez l'angle de rotation (90°, 180°, ...) :";
    }

    @Override
    public void setParametre(Object param) {
        if (param instanceof Integer)
            setDegres((Integer) param);
    }

    @Override
    public Object clone() {
        EtapeRotation clone = null;
        // On récupère l'instance à renvoyer par l'appel de la méthode super.clone()
        clone = (EtapeRotation) super.clone();
        clone.setSens(sens);
        clone.setDegres(degres);
        // on renvoie le clone
        return clone;
    }

}