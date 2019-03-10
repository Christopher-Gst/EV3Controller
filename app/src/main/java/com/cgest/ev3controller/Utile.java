package com.cgest.ev3controller;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Map;

public final class Utile {

    public static boolean connexionBluetoothEtablie = false;

    /**
     * Permet d'appliquer la police principale de l'application "fontdinerdotcom_huggable" aux vues de l'application.
     * S'applique aussi aux Buttons car Button hérite de TextView.
     *
     * @param context
     * @param textView Vue sur laquelle appliquer la police.
     */
    public static void appliquerPolicePrincipale(final Context context, final TextView textView) {
        // Concernent aussi les boutons car Button hérite de TextView
        Typeface policePrincipale = Typeface.createFromAsset(context.getAssets(), "fonts/fontdinerdotcom_huggable.TTF");
        textView.setTypeface(policePrincipale);
    }

    public static boolean scannerQrCodeInstalle(Context context) {
        try {
            context.getPackageManager().getPackageInfo("com.google.zxing.client.android", 0);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

}