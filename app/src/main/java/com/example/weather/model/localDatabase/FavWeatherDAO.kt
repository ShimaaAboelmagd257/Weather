package com.example.weather.model.localDatabase

import androidx.room.*
import com.example.weather.model.pojos.UserLocation
import kotlinx.coroutines.flow.Flow
@Dao
interface FavWeatherDAO {

    @Query("SELECT * FROM UserLocation  where isFavourite = 1")
    fun getAllFavs(): Flow<List<UserLocation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFav(favLocation: UserLocation)

    @Delete
    suspend fun deleteFav(favLocation: UserLocation)

}