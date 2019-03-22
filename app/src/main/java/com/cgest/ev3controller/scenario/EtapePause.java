package com.cgest.ev3controller.scenario;

import com.cgest.ev3controller.Utile;

public class EtapePause extends Etape implements EtapeParametrable {

    private int duree;
    // Valeur par défaut pour le constructeur vide.
    private final static int DUREE_PAR_DEFAUT = 3;

    public EtapePause(int duree) {
        this.duree = duree;
    }

    @Override
    public String getCode() {
        return "P." + duree;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public EtapePause() {
        this.duree = DUREE_PAR_DEFAUT;
    }

    @Override
    public Integer getParamType() {
        return new Integer(0);
    }

    @Override
    public String getTexteAvecDetailsDescription() {
        return String.valueOf(duree);
    }

    public String getTexteDescription() {
        return "";
    }

    public int getIdImageDescription() {
        return Utile.getIdDrawableAvecNom("icon_sablier" + Utile.getSuffixeNomImageAction());
    }

    @Override
    public String getMessageEdition() {
        return "Saisissez la durée de la pause :";
    }

    @Override
    public void setParametre(Object param) {
        if (param instanceof Integer)
            setDuree((Integer) param);
    }

}