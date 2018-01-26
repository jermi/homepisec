package org.homepisec.android.homepisecapp

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceActivity
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.Toast
import org.homepisec.android.homepisecapp.preferences.GeneralSettingsFragment

open class CustomDrawerActivity : AppCompatActivity() {
    private var drawerToggle: ActionBarDrawerToggle? = null
    protected fun setupDrawer() {
        val listView: ListView = findViewById(R.id.navList)
        val drawerItems: List<DrawerItem> = listOf(
                DrawerItem(DrawerItem.READINGS, R.drawable.ic_tune_black_24dp, "Readings"),
                DrawerItem(DrawerItem.SETTINGS, R.drawable.ic_power_black_24dp, "Settings")
        )

        val draweLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        listView.adapter = DrawerItemAdapter(this, drawerItems, { menuId ->
            draweLayout.closeDrawer(GravityCompat.START)
            //            val activityClass = if(DrawerItem.READINGS == menuId) then
            //                MainActivity.class else SettingsActivity.class
            //            }

            val targetActivityClass: Class<*> = when (menuId) {
                DrawerItem.READINGS -> MainActivity::class.java
                DrawerItem.SETTINGS -> SettingsActivity::class.java
                else -> throw IllegalArgumentException("invalid menu id " + menuId)
            }

            val intent = Intent(this, targetActivityClass)
            intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, GeneralSettingsFragment.FRAGMENT_NAME)
            intent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true)
            startActivity(intent)
        })
        drawerToggle = object : ActionBarDrawerToggle(
                this, /* host Activity */
                draweLayout, /* DrawerLayout object */
                R.string.drawer_open, /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state.  */
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
    //                invalidateOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state.  */
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
    //                invalidateOptionsMenu()
            }
        }

        draweLayout!!.addDrawerListener(drawerToggle as ActionBarDrawerToggle)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle!!.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle!!.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        return if (drawerToggle!!.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
        // Handle your other action bar items...

    }
}