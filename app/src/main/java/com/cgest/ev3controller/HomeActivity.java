package com.cgest.ev3controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private Button btnHomeScenarioMode;
    private Button btnHomeManualMode;

    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // On récupère les boutons de mode.
        btnHomeScenarioMode = (Button) findViewById(R.id.btnHomeScenarioMode);
        btnHomeManualMode = (Button) findViewById(R.id.btnHomeManualMode);

        String htmlScenarioMode = getResources().getString(R.string.home_scenario_mode);
        String htmlManualMode = getResources().getString(R.string.home_manual_mode);

        Log.e(TAG, htmlScenarioMode);
        Log.e(TAG, htmlManualMode);

        // On affiche le texte formaté (gras, italique), dans les boutons.
        btnHomeScenarioMode.setText(Html.fromHtml(htmlScenarioMode));
        btnHomeManualMode.setText(Html.fromHtml(htmlManualMode));
    }
}
