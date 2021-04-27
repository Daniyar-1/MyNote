package com.example.mynote.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private SharedPreferences sharedPreferences;

    public Prefs(Context context) {
        sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    public void setShown() {
        sharedPreferences.edit().putBoolean("isShown", true).apply();

    }

    public boolean isShown() {
        return sharedPreferences.getBoolean("isShown", false);
    }
}
