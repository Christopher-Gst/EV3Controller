package com.cgest.ev3controller.scenario;

public class EtapeArreter extends Etape {

    @Override
    public String getCode() {
        return "ARRET";
    }

    @Override
    public String getTexteAvecDetailsDescription() {
        return "";
    }

    public String getTexteDescription() {
        return "Arrêter";
    }

}