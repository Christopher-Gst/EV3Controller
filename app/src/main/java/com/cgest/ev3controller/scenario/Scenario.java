package com.cgest.ev3controller.scenario;

import com.cgest.ev3controller.capteur.CapteurCouleur;
import com.cgest.ev3controller.capteur.CapteurProximite;
import com.cgest.ev3controller.capteur.CapteurToucher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Scenario {

    // Nom du scénario, enregistré dans les préférences partagées de l'application.
    private String nom;

    // Liste des étapes du scénario.
    private ArrayList<Etape> etapes;

    /*
    Types d'actions que l'utilisateur peut ajouter à son scénario, qui apparaissent dans la liste de gauche du mode manuel.
    Ces types d'actions correspondent aux cartes de QR code mises à disposition sur le stand du robot.
     */
    public static final List<Etape> TYPES_ACTIONS_UTILISABLES = Collections.unmodifiableList(
            Arrays.asList(
                    new Etape[]{
                            new EtapeAvancer(0, EtapeAvancerReculer.SECONDES), // Avancer pendant une durée
                            new EtapeAvancer(0, EtapeAvancerReculer.CM), // Avancer une distance
                            new EtapeAvancer(new CapteurProximite(0)), // Avancer jusqu'à détection d'un objet à proximité (à l'avant)
                            new EtapeAvancer(new CapteurToucher()), // Avancer jusqu'à détection d'un toucher à l'arrière
                            new EtapeAvancer(new CapteurCouleur()), // Avancer jusqu'à détection d'une certaine couleur
                            new EtapeReculer(0, 0), // Reculer pendant une durée
                            new EtapeReculer(0, 1), // Reculer une distance
                            new EtapeReculer(new CapteurProximite(0)), // Reculer jusqu'à détection d'un objet à proximité (à l'avant)
                            new EtapeReculer(new CapteurToucher()), // Reculer jusqu'à détection d'un toucher à l'arrière
                            new EtapeReculer(new CapteurCouleur()), // Reculer jusqu'à détection d'une certaine couleur
                            new EtapeRotation(EtapeRotation.DROITE, 0), // Faire une rotation à droite de tant de degrés
                            new EtapeRotation(EtapeRotation.GAUCHE, 0), // Faire une rotation à gauche de tant de degrés
                            //new EtapeRotation(EtapeRotation.BAISSER, 0), // Saisir, attraper un objet.
                            //new EtapeRotation(EtapeRotation.MONTER, 0), // Lâcher, laisser un objet.
                            new EtapePause(0), // Faire une pause de telle ou telle durée
                            new EtapeBip(), // Faire une "bip"
                            new EtapeMusique() // Jouer une mélodie
                    }
            )
    );

    public Scenario() {
        /* Par défaut, le scénario n'a pas de nom. Il est récupéré dans les fichiers de préférences,
        grâce aux méthodes "obtenirScenario()" ou "getTableauNomsScenarios()" de la classe
        ScenarioManager. */
        nom = "";
        etapes = new ArrayList<Etape>();
    }

    /**
     * Permet d'ajouter une étape au scénario.
     * @param etape Etape à ajouter au scénario.
     */
    public void ajouterEtape(Etape etape) {
        etapes.add(etape);
    }

    /**
     * Permet de supprimer une étape du scénario.
     * @param etape Etape à supprimer du scénario.
     * @return True si le scénario contenait l'étape spécifiée, sinon False.
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
     * Permet d'obtenir le code du scénario. Le code est constitué de la concaténation des
     * codes des étapes du scénario, séparés par un point-virgule. Attention, il n'y a pas de point-virgule
     * à la fin du code.
     * @return Le code du scénario.
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
     * Permet d'obtenir une instance du scénario dont le code est passé en paramètre.
     * @param code Le code du scénario.
     * @return Une instance du scénario dont le code est passé en paramètre.
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

    /**
     * Permet d'obtenir la liste des étapes du scénario.
     * @return La liste des étapes du scénario.
     */
    public ArrayList<Etape> getEtapes() {
        return etapes;
    }

}