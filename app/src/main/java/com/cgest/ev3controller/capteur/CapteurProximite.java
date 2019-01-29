package com.cgest.ev3controller.capteur;

public class CapteurProximite extends Capteur {

    private int distanceDetection;

    public CapteurProximite(int distanceDetection) {
        this.distanceDetection = distanceDetection;
    }

    @Override
    public String getCode() {
        return "P." + distanceDetection;
    }
}