package com.cgest.ev3controller.scenario;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ScenarioManager {

    private final static String NOM_PREFS = "Scenarios";

    public static boolean verifierExistenceScenario(Context context, String nom) {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(NOM_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        return pref.contains(nom);
    }

    public static void enregistrerScenario(Context context, Scenario scenario) {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(NOM_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(scenario.getNom(), scenario.getCode());

        editor.commit();
    }

    public static String[] getTableauNomsScenarios(Context context, String nomAIgnorer) {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(NOM_PREFS, MODE_PRIVATE);

        // On récupère une liste associative entre les clés et les valeurs du fichier de préférences partagées.
        Map<String, ?> scenarios = pref.getAll();

        // On crée la liste de noms.
        String[] noms = new String[scenarios.size()];

        // Pour chaque couple clé-valeur, on n'ajoute que la clé à la liste des noms.
        int i = 0;
        for (Map.Entry<String, ?> entry : scenarios.entrySet()) {
            if (!entry.getKey().equals(nomAIgnorer)) {
                noms[i] = entry.getKey();
                i++;
            }
        }

        // On crée un nouveau tableau ne contenant pas les éventuelles chaînes vides ou nulles (à cause du décalage lié au
        // nom à ignorer qui peut être "" si le scénario est nouveau).
        int nbNoms = 0;
        for (int j = 0; j < noms.length; j++)
            if (noms[j] != null && !noms[j].equals(""))
                nbNoms++;
        String[] nomsCorrects = new String[nbNoms];
        for (int j = 0; j < nbNoms; j++)
            nomsCorrects[j] = noms[j];

        // On retourne la conversion de la liste en tableau.
        return nomsCorrects;
    }

    public static Scenario obtenirScenario(Context context, String nomScenario) {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(NOM_PREFS, MODE_PRIVATE);

        Scenario scenario = Scenario.getScenarioFromCode(pref.getString(nomScenario, ""));
        scenario.setNom(nomScenario);

        return scenario;
    }

    public static void supprimerScenario(Context context, String nomScenario) {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(NOM_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.remove(nomScenario);

        editor.commit();
    }
}