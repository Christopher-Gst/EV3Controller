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

    public int getIdImageDescription() {
        return Utile.getIdDrawableAvecNom("icon_musique" + Utile.getSuffixeNomImageAction());
    }

}