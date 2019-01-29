package com.cgest.ev3controller;

public class EtapeRotation extends Etape {

    // Rotation à droite ou à gauche repérée par des constantes.
    private int sens;
    public static int DROITE = 0;
    public static int GAUCHE = 1;
    // Degré de rotation à effectuer.
    private int degres;

    public EtapeRotation(int sens, int degres) {
        this.sens = sens;
        this.degres = degres;
    }

    @Override
    public String getCode() {
        return "ROT." + sens + "." + degres;
    }
}