package com.cgest.ev3controller.capteur;

import com.cgest.ev3controller.Utile;

public class CapteurToucher extends Capteur {

    @Override
    public String getCode() {
        return "T";
    }

    @Override
    public Object getParamType() {
        return null;
    }

    public int getIdImageDescription() {
        return Utile.getIdDrawableAvecNom("icon_capteur_toucher" + Utile.getSuffixeNomImageAction());
    }

}