package com.cgest.ev3controller.scenario;

public class EtapePause extends Etape {

    private int duree;

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
}