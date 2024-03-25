package com.example.weather.model.repository

import android.util.Log
import com.example.weather.model.localDatabase.LocalSouece
import com.example.weather.model.pojos.ApiResponse
import com.example.weather.model.pojos.UserLocation
import com.example.weather.model.pojos.UserWeatherAlert
import com.example.weather.model.remoteDatabase.RemoteSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class Repository private constructor(var remoteSource: RemoteSource, var localSource: LocalSouece):RepoInterface {

    //val favoriteLocations: Flow<List<UserLocation>> = FavWeatherDAO.getAllFavs()
    companion object {
        private var instance: Repository? = null
        fun getInstance(remoteSource: RemoteSource, localSource: LocalSouece): Repository {
            return instance ?: synchronized(this) {
                val INSTANCE = Repository(remoteSource,localSource)
                instance = INSTANCE
                INSTANCE
            }
        }
    }
    override suspend fun getApiCallResponse(
        lat: Double?,
        lon: Double?,
        units: String?,
        lang: String?
    ): ApiResponse{
        Log.d("WeatherRepository", "getApiCallResponse succeeded"+ lat + lon  )

        return remoteSource.getApiCallResponse(lat, lon, units, lang)
         /*catch (e: Exception){
            Log.e("WeatherRepository", "Error fetching weather data: ${e.message}")
            null
        }*/
    }

    override fun getCurrentWeather(): Flow<List<ApiResponse>> {
         return localSource.getCurrentWeather()
    }

    override suspend fun insertCurrentWeather(weather: ApiResponse) {
        Log.d("WeatherRepository", "insertCurrentWeather succeeded"+ weather?.lon  + weather?.lat )
        localSource.insertCurrentWeather(weather)
    }

    override suspend fun deleteCurrentWeather() {
        localSource.deleteCurrentWeather()
    }

    override fun getAllFavs(): Flow<List<UserLocation>> {
        Log.d("WeatherRepository", "getAllFavs succeeded" )
        return localSource.getAllFavs()
    }

    override suspend fun insertFav(favLocation: UserLocation) {
        withContext(Dispatchers.IO) {
            localSource.insertFav(favLocation)
            Log.d("WeatherRepository", "insertFav succeeded" + favLocation.lat + favLocation.lon)
        }
    }

    override suspend fun deleteFav(favLocation: UserLocation) {
        Log.d("WeatherRepository", "deleteFav succeeded" + favLocation.lon )
        localSource.deleteFav(favLocation)
    }

    override fun getAllAlerts(currentTime: Long): Flow<List<UserWeatherAlert>> {
        return localSource.getAllAlerts(currentTime)
    }

    override suspend fun insertAlert(userWeatherAlert: UserWeatherAlert) {
        localSource.insertAlert(userWeatherAlert)
    }

    override suspend fun deletePastAlerts(currentTime: Long) {
        localSource.deletePastAlerts(currentTime)
    }

    override suspend fun deleteAlertItem(userWeatherAlert: UserWeatherAlert) {
        localSource.deleteAlert(userWeatherAlert)
    }
}