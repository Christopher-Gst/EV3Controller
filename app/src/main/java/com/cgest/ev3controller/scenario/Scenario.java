package com.cgest.ev3controller.scenario;

import java.util.ArrayList;
import java.util.Collections;

public class Scenario {
    private ArrayList<Etape> etapes;

    public Scenario() {
        etapes = new ArrayList<Etape>();
    }

    public void ajouterEtape(Etape etape) {
        etapes.add(etape);
    }

    public boolean supprimerEtape(Etape etape) {
        return etapes.remove(etape);
    }

    public void intervertirEtapes(Etape etapeA, Etape etapeB) {
        Collections.swap(etapes, etapes.indexOf(etapeA), etapes.indexOf(etapeB));
    }

    public String getCode() {
        String code = "";
        for (Etape etape : etapes)
            code += etape.getCode() + ";";
        // On supprimer le dernier ";".
        code = code.substring(0, code.length() - 1);
        return code;
    }

    public ArrayList<Etape> getEtapes() {
        return etapes;
    }
}