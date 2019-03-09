package com.cgest.ev3controller;

import android.os.AsyncTask;

public class InitBluetoothTask extends AsyncTask<ChoixModeActivity, Void, String> {

    ChoixModeActivity activity;

    @Override
    protected String doInBackground(ChoixModeActivity... params) {
        activity = params[0];
        return Ev3BluetoothManager.initialiserLeManager(params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result == null) {
            activity.cacherErreurBluetooth();
            activity.afficherModes();
        } else activity.afficherErreurBluetooth(result);
    }
}