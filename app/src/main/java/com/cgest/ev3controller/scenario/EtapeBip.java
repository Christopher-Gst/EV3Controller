package com.cgest.ev3controller.scenario;

import com.cgest.ev3controller.Utile;

public class EtapeBip extends Etape {

    @Override
    public String getCode() {
        return "B";
    }

    @Override
    public String getTexteAvecDetailsDescription() {
        return "";
    }

    public String getTexteDescription() {
        return "";
    }

    public String getNomImageDescription() {
        return "icon_cloche" + super.getNomImageDescription();
    }

}