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

    public String getNomImageDescription() {
        return "icon_sablier" + super.getNomImageDescription();
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


    @Override
    public Object clone() {
        EtapePause clone = null;
        // On récupère l'instance à renvoyer par l'appel de la méthode super.clone()
        clone = (EtapePause) super.clone();
        clone.setDuree(duree);
        // on renvoie le clone
        return clone;
    }

}