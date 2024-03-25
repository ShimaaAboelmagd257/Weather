package com.example.weather.model.localDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather.model.pojos.ApiResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentWeatherDAO {

    @Query("Select * from ApiResponse")
    fun  getCurrentWeather(): Flow<List<ApiResponse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(weather: ApiResponse)

    @Query("Delete from ApiResponse")
    suspend fun deleteCurrentWeather()
}