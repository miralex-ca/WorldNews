package com.muralex.worldnews.presentation

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.ui.*
import com.muralex.worldnews.R
import com.muralex.worldnews.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

interface TempToolbarTitleListener {
    fun updateTitle(title: String)
}

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), TempToolbarTitleListener,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        // requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.nav_home, R.id.nav_bookmarks, R.id.nav_settings),
            drawerLayout = drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.navView.apply {
            setupWithNavController(navController)
            setNavigationItemSelectedListener(this@MainActivity)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val selectItem = true
        when (item.itemId) {
            R.id.nav_home -> navController.popBackStack(R.id.nav_home, false)
            R.id.nav_bookmarks -> {
                navController.popBackStack(R.id.nav_home, false)
                navController.navigate(R.id.nav_bookmarks, null, getNavOptions())
            }
            else -> NavigationUI.onNavDestinationSelected(item, navController)
        }

        lifecycleScope.launch {
            delay(90)
            binding.drawerLayout.closeDrawers()

        }


        return selectItem

    }

    private fun getNavOptions() : NavOptions{
        return  NavOptions
            .Builder()
            .setEnterAnim(R.anim.fade_in)
            .setExitAnim(R.anim.fade_exit_out)
            .setPopEnterAnim(R.anim.fade_in)
            .setPopExitAnim(R.anim.fade_exit_out)
            .build()
    }

    override fun updateTitle(title: String) {
        supportActionBar?.title = title
    }


}