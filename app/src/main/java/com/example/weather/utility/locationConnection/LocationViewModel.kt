package com.example.weather.utility.locationConnection

import android.app.Activity
import android.app.Application
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather.model.pojos.UserLocation
import com.google.android.gms.location.*

class LocationViewModel(application: Application ) : AndroidViewModel(application) {


    private lateinit var  mFusedLocationClient: FusedLocationProviderClient
   var locationLiveData: MutableLiveData<UserLocation> = MutableLiveData()


    private val _desiredLocationLiveData = MutableLiveData<UserLocation>()
    val desiredLocationLiveData: LiveData<UserLocation>
        get() = _desiredLocationLiveData
    private val locationUtils = LocationUtils(application)


    init {
        initializeFusedLocationClient(application)
    }

    private fun initializeFusedLocationClient(application: Application) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
    }

    fun checkLocationPermissions(): Boolean {
        return locationUtils.checkPermissions()
    }

    fun requestLocationPermissions(activity: Activity) {
        locationUtils.requestLocationPermissions(activity)
    }

    fun isLocationEnabled(): Boolean {
        return locationUtils.isLocationEnabled()
    }
    @Suppress("MissingPermission")
    //it's more of a tool to acknowledge that you are aware of the potential issue.
    fun requestNewLocationData() {
        val mLocationRequest = LocationRequest.create()
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setInterval(10000)
       // mFusedLocationClient = LocationServices.getFusedLocationProviderClient()
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest,
            locationCallback,
            Looper.myLooper()
        )

    }
    fun updateDesiredLocation(latitude: Double, longitude: Double) {
        val location = UserLocation(false,latitude, longitude)
        _desiredLocationLiveData.value = location
        Log.d("WeatherLocationvm", "Desired Location latitude and longitude $latitude $longitude")
    }



/*
    fun insertFavLocation(latitude: Double, longitude: Double) {
        val location = UserLocation(true, latitude, longitude)
        viewModelScope.launch {
            repo.insertFav(location)
            Log.d("WeathearFav", "insertFavLocation: ${location.lat}, ${location.lon}")
        }
    }*/
  /* fun addFavoriteLocation(latitude: Double, longitude: Double) {
       val location = UserLocation(true, latitude, longitude)
       viewModelScope.launch {
           repo.insertFav(location)
           Log.d("WeatherLocationViewModel", "insertFav to room: ${location.isFavourite}")

           // Fetch the updated list of favorite locations from the repository
           repo.getAllFavs().collect { updatedLocations ->
               _favLocationLiveData.value = updatedLocations
               Log.d("WeatherLocationViewModel", "updateFavLocation: ${updatedLocations.size}")
           }
       }
   }*/
  /* fun addFavoriteLocation(latitude: Double, longitude: Double) {
       val location = UserLocation(true, latitude, longitude)
       val currentLocations = _favLocationLiveData.value?.toMutableList() ?: mutableListOf()
       currentLocations.add(location)
       _favLocationLiveData.value = currentLocations.toList()

       viewModelScope.launch {
           currentLocations.forEach { newLocation ->
               repo.insertFav(newLocation)
               Log.d("WeatherLocationViewModel", "insertFav to room: ${newLocation.isFavourite}")
           }
       }
   }*/


     val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val myLocation = locationResult.lastLocation
            myLocation?.let {
                val location = UserLocation(false,it.latitude, it.longitude)
                locationLiveData.value = location
                Log.d("WeatherLocationvm", "User Location latitude and longitude" +it.latitude.toString() + it.longitude.toString())

            }
        }
    }
    fun stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(locationCallback)
    }
}