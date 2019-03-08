package com.cgest.ev3controller.scenario;

public class EtapePause extends Etape {

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
}