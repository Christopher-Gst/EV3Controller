package com.cgest.ev3controller.scenario;

import java.util.ArrayList;
import java.util.Collections;

public class Scenario {

    // Nom du scénario, enregistré dans les préférences partagées de l'application.
    private String nom;

    private ArrayList<Etape> etapes;

    public Scenario() {
        nom = "";
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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public ArrayList<Etape> getEtapes() {
        return etapes;
    }

    public static Scenario getScenarioFromCode(String code) {
        Scenario sc = new Scenario();
        if (code.contains(";")) {
            String[] etapesStr = code.split(";");
            for (String monEtapeStr : etapesStr) {
                Etape etape = Etape.getEtapeFromCode(monEtapeStr);
                sc.ajouterEtape(etape);
            }
        } else {
            Etape etape = Etape.getEtapeFromCode(code);
            sc.ajouterEtape(etape);
        }
        return sc;
    }

}