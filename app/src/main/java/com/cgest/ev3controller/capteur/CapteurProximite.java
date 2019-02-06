package com.cgest.ev3controller.capteur;

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
}