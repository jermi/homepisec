package org.homepisec.android.homepisecapp.preferences;

import android.os.Bundle
import android.preference.PreferenceFragment
import org.homepisec.android.homepisecapp.R

class SettingsFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
    }
}
