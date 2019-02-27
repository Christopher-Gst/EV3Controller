package com.cgest.ev3controller;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

public final class Utile {

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

}