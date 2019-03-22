package com.cgest.ev3controller.scenario;

import com.cgest.ev3controller.Utile;

public class EtapeRotation extends Etape implements EtapeParametrable {

    // Rotation à droite ou à gauche repérée par des constantes.
    private int sens;
    public final static int DROITE = 0;
    public final static int GAUCHE = 1;
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
        return "Tourner";
    }

    public String getTexteDescription() {
        return "Tourner";
    }

    public int getIdImageDescription() {
        return Utile.getIdDrawableAvecNom("icon_" + (sens == DROITE ? "droite" : "gauche") + Utile.getSuffixeNomImageAction());
    }

    @Override
    public String getMessageEdition() {
        return "Saisissez l'angle de rotation (90°, 180°, ...) :";
    }

    @Override
    public void setParametre(Object param) {
        if (param instanceof Integer)
            setDegres((Integer) param);
    }
}