package com.example.weather.model.localDatabase


import com.example.weather.model.pojos.ApiResponse
import com.example.weather.model.pojos.UserLocation
import com.example.weather.model.pojos.UserWeatherAlert
import kotlinx.coroutines.flow.Flow

interface LocalSouece {


    fun  getCurrentWeather(): Flow<List<ApiResponse>>
    suspend fun insertCurrentWeather(weather: ApiResponse)
    suspend fun deleteCurrentWeather()


    fun getAllFavs(): Flow<List<UserLocation>>
    suspend fun insertFav(favLocation: UserLocation)
    suspend fun deleteFav(favLocation: UserLocation)


    fun getAllAlerts(currentTime: Long): Flow<List<UserWeatherAlert>>
    suspend fun insertAlert(userWeatherAlert: UserWeatherAlert)
    suspend fun deletePastAlerts(currentTime: Long)
    suspend fun deleteAlert(userWeatherAlert: UserWeatherAlert)

}