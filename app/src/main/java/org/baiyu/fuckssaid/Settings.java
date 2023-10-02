package org.baiyu.fuckssaid;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

public class Settings {

    private static SharedPreferences prefs;

    private volatile static Settings INSTANCE;

    private static final String PREF_SSAID = "ssaid";

    private Settings(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
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

    @Nullable
    public String getIdForPackage() {
        return prefs.getString(PREF_SSAID, null);
    }

    public void setIdForPackage(String id) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_SSAID, id);
        editor.apply();
    }
}
