package com.cgest.ev3controller.capteur;

public enum Couleur {

    BLANC, NOIR, ROSE, BLEU, VERT, JAUNE, ORANGE, ROUGE, MARRON;

    @Override
    public String toString() {
        return super.toString();
    }

    public int getIdInEnum() {
        switch (this) {
            case BLANC:
                return 0;
            case NOIR:
                return 1;
            case ROSE:
                return 2;
            case BLEU:
                return 3;
            case VERT:
                return 4;
            case JAUNE:
                return 5;
            case ORANGE:
                return 6;
            case ROUGE:
                return 7;
            case MARRON:
                return 8;
            default:
                return -1;
        }
    }

}