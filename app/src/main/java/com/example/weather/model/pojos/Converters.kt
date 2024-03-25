package com.example.weather.model.pojos

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromCurrentWeather(currentWeather: CurrentWeather?): String {
        // Convert CurrentWeather to String (serialization)
        return Gson().toJson(currentWeather)
    }

    @TypeConverter
    fun toCurrentWeather(json: String?): CurrentWeather {
        // Convert String to CurrentWeather (deserialization)
        return Gson().fromJson(json, CurrentWeather::class.java)
    }

    @TypeConverter
    fun fromMinuteForecastList(minuteForecastList: List<MinuteForecast>?): String {
        return Gson().toJson(minuteForecastList)
    }

    @TypeConverter
    fun toMinuteForecastList(json: String): List<MinuteForecast>? {
        val typeToken = object : TypeToken<List<MinuteForecast>>() {}.type
        return Gson().fromJson(json, typeToken)
    }
    @TypeConverter
    fun fromHourlyForecastList(hourlyForecastList: List<HourlyForecast>?): String {
        return Gson().toJson(hourlyForecastList)
    }

    @TypeConverter
    fun toHourlyForecastList(json: String): List<HourlyForecast>? {
        val typeToken = object : TypeToken<List<HourlyForecast>>() {}.type
        return Gson().fromJson(json, typeToken)
    }
    @TypeConverter
    fun fromDailyForecast(dailyForecast: List<DailyForecast>?): String {
        return Gson().toJson(dailyForecast)
    }

    @TypeConverter
    fun toDailyForecastList(json: String?): List<DailyForecast> {
        val typeToken = object : TypeToken<List<DailyForecast>>() {}.type
        return Gson().fromJson(json, typeToken)
    }
    @TypeConverter
    fun fromAlert(alert: List<Alert>?): String {
        return Gson().toJson(alert)
    }

    @TypeConverter
    fun toAlertList(json: String?): List<Alert> {
        try {
            if (json.isNullOrEmpty()) {
                return emptyList()
            }

            val typeToken = object : TypeToken<List<Alert>>() {}.type
            return Gson().fromJson(json, typeToken) ?: emptyList()
        } catch (e: Exception) {
            Log.e("Converters", "Error parsing JSON: $json", e)
            return emptyList()
        }
    }



}