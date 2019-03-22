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

    public int getIdImageDescription() {
        return Utile.getIdDrawableAvecNom("icon_cloche" + Utile.getSuffixeNomImageAction());
    }

}