package com.cgest.ev3controller.capteur;

public class CapteurToucher extends Capteur {

    @Override
    public String getCode() {
        return "T";
    }

    @Override
    public Object getParamType() {
        return null;
    }

}