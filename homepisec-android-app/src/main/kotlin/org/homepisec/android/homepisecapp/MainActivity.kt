package org.homepisec.android.homepisecapp

import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView


class MainActivity : AppCompatActivity() {

    var drawerToggle: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView: ListView = findViewById(R.id.navList)
        val drawerItems: List<DrawerItem> = listOf(
                DrawerItem(DrawerItem.READINGS, R.drawable.ic_tune_black_24dp, "Readings"),
                DrawerItem(DrawerItem.SETTINGS, R.drawable.ic_power_black_24dp, "Settings")
        )
        listView.adapter = DrawerItemAdapter(this, drawerItems)

        val draweLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
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

        val mDrawerContent: NavigationView = findViewById(R.id.navigation);

//        // Initialize your data adapter
//        mMenuDataAdapter = new MenuDataAdapter (MenuHolder.getInstance().getMenuItemList());
//        // Find recyclerview
//        mRecyclerView = (RecyclerView) findViewById (R.id.left_drawer);
//        // Define layout manager
//        mRecyclerView.setLayoutManager(new LinearLayoutManager (this));
//        // Set adapter
//        mRecyclerView.setAdapter(mMenuDataAdapter);
//        // Set animator
//        mRecyclerView.setItemAnimator(new SlideInUpAnimator (new OvershootInterpolator (1f)));

//        listView = findViewById(R.id.left_drawer)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
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
