package com.example.weather.model.localDatabase

import androidx.room.*
import com.example.weather.model.pojos.UserWeatherAlert
import kotlinx.coroutines.flow.Flow
@Dao
interface AlertDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlert(userWeatherAlert: UserWeatherAlert)

    @Query("SELECT * FROM UserWeatherAlert WHERE (timeFrom >= :currentTime)")
    fun getAllAlerts(currentTime: Long): Flow<List<UserWeatherAlert>>

    @Query("DELETE  FROM UserWeatherAlert WHERE timeTo <= :currentTime")
    suspend fun deletePastAlerts(currentTime: Long)

    @Delete
    suspend fun deleteAlert(userWeatherAlert: UserWeatherAlert)
}