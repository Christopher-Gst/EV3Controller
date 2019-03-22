package com.cgest.ev3controller.capteur;

import com.cgest.ev3controller.Utile;

public class CapteurProximite extends Capteur {

    public final static int DISTANCE_PAR_DEFAUT = 20;
    private int distanceDetection;

    public CapteurProximite(int distanceDetection) {
        this.distanceDetection = distanceDetection;
    }

    public CapteurProximite() {
        this.distanceDetection = DISTANCE_PAR_DEFAUT;
    }

    @Override
    public String getCode() {
        return "P." + distanceDetection;
    }

    public int getDistanceDetection() {
        return distanceDetection;
    }

    public void setDistanceDetection(int distanceDetection) {
        this.distanceDetection = distanceDetection;
    }

    @Override
    public Integer getParamType() {
        return new Integer(0);
    }

    public int getIdImageDescription() {
        return Utile.getIdDrawableAvecNom("icon_capteur_obstacle" + Utile.getSuffixeNomImageAction());
    }
}