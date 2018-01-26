package org.homepisec.android.homepisecapp

import android.os.Bundle
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment

class SettingsActivity : PreferenceActivity() {

    override fun onBuildHeaders(target: List<PreferenceActivity.Header>) {
        loadHeadersFromResource(R.xml.settings, target)
    }

    override fun isValidFragment(fragmentName: String?): Boolean {
        return GeneralSettingsFragment.FRAGMENT_NAME == fragmentName
    }

    class GeneralSettingsFragment : PreferenceFragment() {
        companion object {
            val FRAGMENT_NAME = "org.homepisec.android.homepisecapp.SettingsActivity\$GeneralSettingsFragment"
        }
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.general_settings)
        }

    }
}