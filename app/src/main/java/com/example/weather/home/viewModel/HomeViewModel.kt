package com.example.weather.home.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.pojos.ApiResponse
import com.example.weather.model.pojos.UserLocation
import com.example.weather.model.repository.RepoInterface
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeViewModel(private val repo :RepoInterface): ViewModel() {

    private var weather = MutableLiveData<List<ApiResponse>>()
    val weatherList:LiveData<List<ApiResponse>>
        get() = weather


    private val _chosenFavLocationLiveData = MutableLiveData<UserLocation>()
    val chosenFavLocationLiveData: LiveData<UserLocation> = _chosenFavLocationLiveData
    fun updateChosenFavLocation(chosenLocation: UserLocation) {
        _chosenFavLocationLiveData.value = chosenLocation
    }

    fun getApiCallResponse(
        lat: Double?,
        lon: Double?,
        units: String?,
        lang: String?
    ) {

        viewModelScope.launch {
            val apiResponse = repo.getApiCallResponse(lat, lon, units, lang)
            Log.d("WeatherHomeViewModel", "Api response: $apiResponse")

            apiResponse.let {
                repo.deleteCurrentWeather()

                    repo.insertCurrentWeather(it)
                  //  it.alerts?.get(0)
                    weather.value = listOf(apiResponse)


                //apiResponse?.alerts
                Log.d("WeatherHomeVM", "getApiCallResponse " +   it.alerts)

            }
        }
    }

     fun loadCachedWeatherData() {
        viewModelScope.launch {
            val cachedData = repo.getCurrentWeather().first()
            weather.postValue(cachedData)
            Log.d("WeatherHomeVM", "loadCachedWeatherData " )

        }
    }

   /*  fun getCurrentWeather() {
        viewModelScope.launch {
           repo.getCurrentWeather()
        }
    }*/


  /*  private fun getCurrentWeatherWithDelay() {
        viewModelScope.launch {
            // Initial API call
            getCurrentWeather()

            // Introduce a delay (e.g., 1 minute) before the next API call
            while (true) {
                delay(60000) // 1 minute delay (adjust as needed)
                getCurrentWeather()
                Log.d("WeatherHomeViewModel", "Api response after one minute delay " )

            }
        }
    }*/

    /*private fun getCurrentWeather() {
        viewModelScope.launch {
            val apiResponse = repo.getCurrentWeather()
            Log.d("WeatherHomeViewModel", "Api response: $apiResponse")
        }
    }*/

/*
    fun  deleteCurrentWeather(){
       viewModelScope.launch {
          weather.postValue(repo.getCurrentWeather())
       }
   }

    fun insertBackupWeather(weather: WeatherData){
        viewModelScope.launch {
            repo.insertCurrentWeather(weather)
        }
    }*/

}