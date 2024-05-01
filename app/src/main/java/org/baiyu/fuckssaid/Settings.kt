package org.baiyu.fuckssaid;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    private static SharedPreferences prefs;

    private volatile static Settings INSTANCE;

    private static final String PREF_SSAID = "ssaid";

    private Settings(Context context) {
        prefs = context.getSharedPreferences(PREF_SSAID, Context.MODE_PRIVATE);
    }

    public static Settings getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (Settings.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Settings(context);
                }
            }
        }
        return INSTANCE;
    }

    public String getIdForPackage() {
        return prefs.getString(PREF_SSAID, null);
    }

    public void setIdForPackage(String id) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_SSAID, id);
        editor.apply();
    }
}
