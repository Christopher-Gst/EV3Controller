package com.cgest.ev3controller;

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