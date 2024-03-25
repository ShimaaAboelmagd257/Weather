package com.example.weather.favorites.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.pojos.UserLocation
import com.example.weather.model.repository.RepoInterface
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repo: RepoInterface) : ViewModel() {


    private val _favLocationsLiveData = MutableStateFlow<List<UserLocation>>(emptyList())
    val favLocationsLiveData: StateFlow<List<UserLocation>> = _favLocationsLiveData

    private val _favLocationLiveData = MutableLiveData<UserLocation>()
    val favLocationLiveData: LiveData<UserLocation>
        get() = _favLocationLiveData

    init {

    getAllFavs()

   }

    fun getAllFavs() {
        viewModelScope.launch {
            repo.getAllFavs().collect { favLocations ->
                _favLocationsLiveData.value = favLocations
                Log.d("WeatherFavVM", "getAllFavs size   " + favLocations.size)

            }
        }
    }
    fun displayavLocation(latitude: Double, longitude: Double) {
        val location = UserLocation(true,latitude, longitude)
        _favLocationLiveData.value = location
        Log.d("WeathearFav", "updateFavLocation: ${location.lat}")

    }
    fun addFavoriteLocation(lat: Double, lon: Double) {
        viewModelScope.launch {
            val userLocation  = UserLocation(true,lat,lon)
            repo.insertFav(userLocation)
            Log.d("WeatherFavVM", "insert Fav  " + lat + lon)

        }
    }

     fun deleteFaveWeather(fav:UserLocation){
         viewModelScope.launch {
             repo.deleteFav(fav)
             Log.d("Weathear FAV Vm", "getAllFavstWeather" +repo.deleteFav(fav).toString())

         }

     }
}

/*
private val _favLocationsLiveData = MutableLiveData<List<UserLocation>>()
  //val favLocationsLiveData: LiveData<List<UserLocation>> = _favLocationsLiveData
  */
/* val favoriteLocations = mutableListOf<UserLocation>()
    private var previousLocation: UserLocation? = null*//*

    init {
      //  getAllFavstWeather()
    }
*/

/* val favoriteLocations: Flow<List<UserLocation>> = repo.getAllFavs().onEach {
     Log.d("WeatherFavVM", "getAllFavstWeather size " + it)

 }*/

/*
    private fun getAllFavstWeather() {
        viewModelScope.launch {
            repo.getAllFavs().collect { favLocations ->
                _favLocationsLiveData.value = favLocations
                Log.d("WeatherFavVM", "getAllFavstWeather size " + repo.getAllFavs())

            }
        }
    }*/
/* fun getAllFavstWeather() {
     viewModelScope.launch {
         repo.getAllFavs().collect { weatherList ->
             if (weatherList.isNotEmpty()) {
                 _favLocationLiveData.postValue(weatherList)
                 previousLocation = weatherList.lastOrNull()
                 Log.d("WeatherFavmmmm", "getAllFavstWeather" + previousLocation?.lon)
             }
         }
     }
 }*/
/*
   fun getAllFavstWeather() {
       viewModelScope.launch {
           repo.getAllFavs().collect { weatherList ->
               if (weatherList.isNotEmpty()) {
                   _favLocationLiveData.postValue(weatherList)
                   Log.d("WeathearFav VM", "getAllFavstWeather" + _favLocationLiveData.value)
               }
           }
       }
   }
*/

/* fun getAllFavstWeather() {
     viewModelScope.launch {
         repo.getAllFavs().collect { weatherList ->
             favweather.postValue(weatherList)
             insertFav(weatherList.first())
             Log.d("WeatherFavmmmm", "getAllFavstWeather" +favweather.value)

         }

     }
 }*/
/*fun isLocationChanged(newLocation: UserLocation): Boolean {
    val lastLocation = favoriteLocations.lastOrNull()
    return lastLocation == null || lastLocation != newLocation
}*/


/*fun insertFav(userLocation: UserLocation) {
   viewModelScope.launch {
       if (isLocationChanged(userLocation)) {
           repo.insertFav(userLocation)
       Log.d("WeathearFAV Vm", "insertFav" + userLocation.lon + userLocation.lat)

    } else {
           Log.d("WeatherFav", "Location not changed, skipping insertion")
       }
   }
}*/