package org.homepisec.android.homepisecapp

import android.os.Bundle

class SettingsActivity : CustomDrawerActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setupDrawer()
    }

}