package com.cgest.ev3controller.scenario;

import java.util.ArrayList;
import java.util.Collections;

public class Scenario {

    // Nom du scénario, enregistré dans les préférences partagées de l'application.
    private String nom;

    // Etapes constituant le scénario.
    private ArrayList<Etape> etapes;

    public Scenario() {
        /* Par défaut, le scénario n'a pas de nom. Il est récupéré dans les fichiers de préférences,
        grâce aux méthodes "obtenirScenario()" ou "getTableauNomsScenarios()" de la classe
        ScenarioManager. */
        nom = "";
        etapes = new ArrayList<Etape>();
    }

    /**
     * Permet d'ajouter une étape au scénario.
     *
     * @param etape L'étape à ajouter.
     */
    public void ajouterEtape(Etape etape) {
        etapes.add(etape);
    }

    /**
     * Permet de supprimer une étape du scénario.
     *
     * @param etape L'étape à supprimer.
     * @return
     */
    public boolean supprimerEtape(Etape etape) {
        return etapes.remove(etape);
    }

    /**
     * Permet d'intervertir deux étapes du scénario.
     *
     * @param etapeA L'une des deux étapes à intervertir.
     * @param etapeB L'autre des deux étapes à intervertir.
     */
    public void intervertirEtapes(Etape etapeA, Etape etapeB) {
        Collections.swap(etapes, etapes.indexOf(etapeA), etapes.indexOf(etapeB));
    }

    /**
     * Permet d'obtenir le code d'un scénario.
     * Les codes des différentes étapes sont séparés par un ";".
     * Par conséquent, il n'y a pas de ";" à la fin de la chaîne.
     *
     * @return
     */
    public String getCode() {
        String code = "";
        for (Etape etape : etapes)
            code += etape.getCode() + ";";
        // On supprimer le dernier ";".
        code = code.substring(0, code.length() - 1);
        return code;
    }

    /**
     * Permet d'obtenir une instance d'un nouveau Scenario à partir de son code.
     *
     * @param code Le code du scénario à obtenir.
     * @return Le scénario associé au code passé en paramètre.S
     */
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

    // GETTERS ET SETTERS //

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public ArrayList<Etape> getEtapes() {
        return etapes;
    }

}