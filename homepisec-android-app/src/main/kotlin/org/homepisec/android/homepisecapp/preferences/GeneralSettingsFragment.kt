package org.homepisec.android.homepisecapp.preferences

import android.os.Bundle
import android.preference.PreferenceFragment
import org.homepisec.android.homepisecapp.R

class GeneralSettingsFragment : PreferenceFragment() {
    companion object {
        val FRAGMENT_NAME = "org.homepisec.android.homepisecapp.preferences.GeneralSettingsFragment"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.general_settings)
    }

}