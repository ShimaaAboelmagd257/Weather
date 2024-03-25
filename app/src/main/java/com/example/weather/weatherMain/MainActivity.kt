package com.example.weather.weatherMain

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.weathear.databinding.ActivityMainBinding
import com.example.weather.utility.locationConnection.LocationViewModel
import com.example.weather.utility.workmanager.AlertBroadcast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var mLocationCallBack: LocationCallback
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var locationViewModel: LocationViewModel

    val receiver = AlertBroadcast()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)


        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        mLocationCallBack = locationViewModel.locationCallback
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
/*
        val alarmHelper = AlarmHelper(this, "Your description", "Your country")
        alarmHelper.onCreate()
*/
        val filter = IntentFilter("com.example.myapp.ALARM_ACTION")
        registerReceiver(receiver, filter)
    }

    override fun onResume() {
        super.onResume()
        getLastLocation()
    }
    override fun onDestroy() {
        super.onDestroy()
        mFusedLocationClient.removeLocationUpdates(mLocationCallBack)
        unregisterReceiver(receiver)
    }
    override fun onStop() {
        super.onStop()
        locationViewModel.stopLocationUpdates()
    }

  /*  private fun getLastLocation() {
        if (locationViewModel.checkLocationPermissions()) {
            if (locationViewModel.isLocationEnabled()) {

            } else {
                showAlertDialog()
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
            }
        } else {
            try {
                locationViewModel.requestNewLocationData()
            } catch (e: Exception) {
                // Handle location request failure
                Log.e("WeatherLocationvm", "Error requesting location updates: ${e.message}")
            }        }
    }*/

  private fun getLastLocation() {
      if (locationViewModel.checkLocationPermissions()) {

      } else {
          try {
             // locationViewModel.requestNewLocationData()
              locationViewModel.requestLocationPermissions(this)
              //showAlertDialog()

          } catch (e: Exception) {
              // Handle location request failure
              Log.e("WeatherLocationvm", "Error requesting location updates: ${e.message}")
          }
      }
  }

   /* private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Alert")
        alertDialogBuilder.setMessage("getString(R.string.ask_permission)")
        alertDialogBuilder.setPositiveButton("R.string.permission_postive_button") { dialog: DialogInterface, _: Int ->
           gotoAppPermission()
        }
        alertDialogBuilder.setNegativeButton("cancel") { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
        }
        alertDialogBuilder.setCancelable(false)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }*/



/*
    private fun gotoAppPermission() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        startActivity(intent)
    }
*/
    private fun gotoAppPermission() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", packageName, null)
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Handle the exception, for example, show a toast message
            Toast.makeText(this, "Could not open application settings", Toast.LENGTH_SHORT).show()
            e.printStackTrace() // Log the exception for debugging purposes
            Log.e("WeatherLocationvm", "Error requesting location updates: ${e.message}")

        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}
//val navController = findNavController(this, R.id.nav_host_fragment)
// NavigationUI.setupActionBarWithNavController(this, navController)
// navController.navigate(R.id.fragmentHome)

/* locationFactory  = LocationFactory( Application(),
        Repository.getInstance(
            WeatherClient.getInstance(), ConcreteLocalSource.getInstance(this)))
    locationViewModel = ViewModelProvider(this,locationFactory).get(LocationViewModel::class.java)*/


/*  override fun onCreateOptionsMenu(menu: Menu): Boolean {
         menuInflater.inflate(R.menu.menu_main, menu)
         return true
     }*/

/*  override fun onOptionsItemSelected(item: MenuItem): Boolean {
      val navController: NavController = findNavController(this, R.id.nav_host_fragment)
      return when (item.itemId) {
          R.id.fragmentset-> NavigationUI.onNavDestinationSelected(item, navController)
          R.id.fragmentAlerts -> NavigationUI.onNavDestinationSelected(item, navController)
          else -> super.onOptionsItemSelected(item)
      }
  }*/

/*override fun onSupportNavigateUp(): Boolean {
    val navController: NavController = findNavController(this, R.id.fragmentHome)
    return navController.navigateUp() || super.onSupportNavigateUp()
}*/

