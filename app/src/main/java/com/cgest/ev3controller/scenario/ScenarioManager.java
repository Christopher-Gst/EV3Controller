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

    public static String[] getTableauNomsScenarios(Context context) {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(NOM_PREFS, MODE_PRIVATE);

        // On récupère une liste associative entre les clés et les valeurs du fichier de préférences partagées.
        Map<String, ?> scenarios = pref.getAll();

        // On crée la liste de noms.
        String[] noms = new String[scenarios.size()];

        // Pour chaque couple clé-valeur, on n'ajoute que la clé à la liste des noms.
        int i = 0;
        for (Map.Entry<String, ?> entry : scenarios.entrySet()) {
            noms[i] = entry.getKey();
            i++;
        }

        return noms;
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

    public static void renommerScenario(Context context, String ancienNom, String nouveauNom) {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(NOM_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        // On récupère le code du scénario.
        String code = pref.getString(ancienNom, "");

        // On supprimer le scénario du fichier.
        editor.remove(ancienNom);

        // On recrée le scénario avec le nouveau nom.
        editor.putString(nouveauNom, code);

        editor.commit();
    }
}