package com.cgest.ev3controller.scenario;

import android.graphics.drawable.Drawable;

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

    public int getIdImageDescription() {
        return 0;
    }

}