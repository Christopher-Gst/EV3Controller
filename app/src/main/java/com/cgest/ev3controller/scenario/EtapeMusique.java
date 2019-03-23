package com.cgest.ev3controller.scenario;

import com.cgest.ev3controller.Utile;

public class EtapeMusique extends Etape {

    @Override
    public String getCode() {
        return "M";
    }

    @Override
    public String getTexteAvecDetailsDescription() {
        return "";
    }

    public String getTexteDescription() {
        return "";
    }

    public String getNomImageDescription() {
        return "icon_musique" + super.getNomImageDescription();
    }

}