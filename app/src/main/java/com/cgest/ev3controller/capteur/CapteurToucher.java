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

    @Override
    public String getNomImageDescription() {
        return "icon_capteur_toucher" + super.getNomImageDescription();
    }

}