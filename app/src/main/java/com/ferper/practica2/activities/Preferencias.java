package com.ferper.practica2.activities;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.ferper.practica2.R;

public class Preferencias extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.activity_preferencias);
    }
}
