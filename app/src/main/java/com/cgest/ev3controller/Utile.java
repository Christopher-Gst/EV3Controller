package com.cgest.ev3controller;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Field;
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

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void setDpHeight(Context context, View view, int height) {
        ViewGroup.LayoutParams param = view.getLayoutParams();
        final float scale = context.getResources().getDisplayMetrics().density;
        //   - on calcule combien font 500dp en pixels.
        param.height = (int) (height * scale + 0.5f);
        view.setLayoutParams(param);
    }

    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

}