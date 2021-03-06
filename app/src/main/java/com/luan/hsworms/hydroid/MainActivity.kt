package com.luan.hsworms.hydroid

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.luan.hsworms.hydroid.backend.notifications.AlarmService
import com.luan.hsworms.hydroid.databinding.ActivityMainBinding
import java.util.*

// TAG for debugging
private const val TAG = "MainActivityFile"

class MainActivity : AppCompatActivity() {

    //Navigation components
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private  lateinit var  navController: NavController

    //AppBar configuration (was passiert, wenn wir clicken)
    private val idSets = setOf(R.id.mainFragment, R.id.settingsFragment, R.id.notificationFragment)
    private lateinit var appBarConfiguration: AppBarConfiguration
    // create a variable to get access to shared preference
    private lateinit var notificationViewModel: NotificationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate called")

        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = binding.drawerLayout
        navView = binding.navView
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(idSets, drawerLayout)

        //Verbinden navControlller und appBarConfiguration
        setupActionBarWithNavController(navController, appBarConfiguration)
        //Verbinden Menu mit NavController
        navView.setupWithNavController(navController)

        /////////////////////////////////////////////////////////////////////////////
        // Notification:
        // Area Reserve for calling the AlarmManager on Creating the application.
        Log.d(TAG, "Entering reserved AlarmManager space")

        // load Data to send an alarm
        notificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        notificationViewModel.notificationPreference = getSharedPreferences("NotificationPreference", Context.MODE_PRIVATE)
        notificationViewModel.loadData()
        // Initial call so the alarm is at least send one time on opening the application.
        AlarmService.setHelpDrinkAlarm(
            this,
            notificationViewModel.canSendHelpDrinkNotification()
        )
        Log.d(TAG, "End of onCreate")
        //////////////////////////////////////////////////////////////////////////
    }

    //Add an Up button in the app bar
    override fun onSupportNavigateUp(): Boolean {
        val navController:NavController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}