package com.cgest.ev3controller.scenario;

public class EtapePause extends Etape {

    private int temps;

    public EtapePause(int temps) {
        this.temps = temps;
    }

    @Override
    public String getCode() {
        return "P";
    }
}