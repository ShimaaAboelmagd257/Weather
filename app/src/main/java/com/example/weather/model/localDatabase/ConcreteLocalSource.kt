package com.example.weather.model.localDatabase

import android.content.Context
import android.util.Log
import com.example.weather.model.pojos.ApiResponse
import com.example.weather.model.pojos.UserLocation
import com.example.weather.model.pojos.UserWeatherAlert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class ConcreteLocalSource(context :Context):LocalSouece {

    private val wetherDatabase: WetherDatabase = WetherDatabase.getInstance(context)
    private val weatherDAO:CurrentWeatherDAO by lazy { wetherDatabase.currentWeatherDAO() }
    private val favoriteDAO:FavWeatherDAO by lazy { wetherDatabase.favWeatherDAO() }
    private  val alertDAO:AlertDAO by lazy { wetherDatabase.alertDAO() }

    companion object {
        @Volatile
        private var INSTANCE : ConcreteLocalSource?=null

        fun getInstance(context: Context): ConcreteLocalSource {
            return INSTANCE?: synchronized(this){
                val instance = ConcreteLocalSource(context)
                INSTANCE=instance
                instance
            }
        }

    }

    override fun getCurrentWeather(): Flow<List<ApiResponse>> {
         return weatherDAO.getCurrentWeather().onEach {
             weathers ->
             Log.d("WeatherLocalSource", "insertCurrentWeather succeeded" + weathers )

         }
    }

    override suspend fun insertCurrentWeather(weather: ApiResponse) {
          weatherDAO.insertCurrentWeather(weather)
        Log.d("WeatherLocalSource", "insertCurrentWeather succeeded" + weather )

    }

    override suspend fun deleteCurrentWeather() {
        weatherDAO.deleteCurrentWeather()
        Log.d("WeatherLocalSource", "deleteCurrentWeather succeeded" )

    }

    override fun getAllFavs(): Flow<List<UserLocation>> {
        return  favoriteDAO.getAllFavs().onEach {
          favorites ->
          Log.d("WeatherLocalSource", "getAllFavs succeeded" +  favorites)

      }
    }

    override suspend fun insertFav(favLocation: UserLocation) {
        favoriteDAO.insertFav(favLocation)
        Log.d("WeatherLocalSource", "insertFav succeeded" +  favLocation)

    }

    override suspend fun deleteFav(favLocation: UserLocation) {
      favoriteDAO.deleteFav(favLocation)
        Log.d("WeatherLocalSource", "deleteFav succeeded" +  favLocation)

    }

    override fun getAllAlerts(currentTime: Long): Flow<List<UserWeatherAlert>> {
        return alertDAO.getAllAlerts(currentTime).onEach {
            alerts ->
            Log.d("WeatherLocalSource", "getAllAlerts succeeded" +  alerts)

        }
    }

    override suspend fun insertAlert(userWeatherAlert: UserWeatherAlert) {
        alertDAO.insertAlert(userWeatherAlert)
        Log.d("WeatherLocalSource", "insertAlert succeeded" +  userWeatherAlert)

    }

    override suspend fun deletePastAlerts(currentTime: Long) {
        alertDAO.deletePastAlerts(currentTime)
        Log.d("WeatherLocalSource", "deletePastAlerts succeeded of " +  currentTime)

    }

    override suspend fun deleteAlert(userWeatherAlert: UserWeatherAlert) {
        alertDAO.deleteAlert(userWeatherAlert)
        Log.d("WeatherLocalSource", "deleteAlert succeeded" +  userWeatherAlert)

    }

}