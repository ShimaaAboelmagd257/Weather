package com.example.weather.model.localDatabase

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weather.model.pojos.*


@Database(entities = [WeatherData::class , UserWeatherAlert::class , ApiResponse::class , UserLocation::class], version = 11)
@TypeConverters(Converters::class)
abstract class WetherDatabase :RoomDatabase() {

    abstract fun alertDAO (): AlertDAO
    abstract fun currentWeatherDAO() : CurrentWeatherDAO
    abstract fun favWeatherDAO() : FavWeatherDAO


    companion object{
        @Volatile
        private var INSTANCE : WetherDatabase? = null
        fun getInstance (context: Context) :WetherDatabase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,WetherDatabase::class.java , "Weather Database"
                ).fallbackToDestructiveMigration()
                    .build()
                Log.d("WeatherDataBase", "singleton instance apply succeeded")
                INSTANCE=instance
                instance
            }
        }
    }


}