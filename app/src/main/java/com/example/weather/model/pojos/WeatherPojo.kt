package com.example.weather.model.pojos

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class WeatherData(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    var isFavourite:Boolean = false,
    val timezone_offset: Int,
    val current: CurrentWeather,
    val minutely: List<MinuteForecast>,
    val hourly: List<HourlyForecast>,
    val daily: List<DailyForecast>,
    val alerts: List<Alert>
) : Parcelable

@Entity
@Parcelize
data class ApiResponse(
    @PrimaryKey(autoGenerate = true)
    val id: Int , // Add a primary key field
    val lat: Double?,
    val lon: Double?,
    val timezone: String?,
    val timezone_offset: Int?,
    val current: CurrentWeather?,
   // val minutely: List<MinuteForecast>?,
    val hourly: List<HourlyForecast>?,
    val daily: List<DailyForecast>?,
    val alerts: List<Alert>?
) : Parcelable
@Entity
@Parcelize
data class UserLocation(
  //  var id: Int = 0,
    var isFavourite:Boolean = false,
    @PrimaryKey
    val lat: Double,
    val lon: Double
) :Parcelable


@Parcelize
data class CurrentWeather(
    @PrimaryKey(autoGenerate = true)
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Double,
    val feels_like: Double,
    val pressure: Double,
    val humidity: Double,
    val dew_point: Double,
    val clouds: Double,
    val visibility: Int,
    val wind_speed: Double,
    val wind_deg: Int,
    val weather: List<WeatherDescription>,
    val uvi: Double
) : Parcelable

@Parcelize
data class MinuteForecast(
    @PrimaryKey(autoGenerate = true)
    val dt: Long,
    val precipitation: Double,
) : Parcelable



@Parcelize
data class HourlyForecast(
    @PrimaryKey(autoGenerate = true)

    val dt: Long,
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Double,
    val clouds: Int,
    val wind_speed: Double,
    val wind_deg: Int,
    val wind_gust: Double,
    val weather: List<WeatherDescription>,
    // Other attributes or nested objects specific to hourly forecast
) : Parcelable


@Parcelize
data class DailyForecast(
    @PrimaryKey(autoGenerate = true)

    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Temperature,
    val feels_like: Temperature,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Double,
    val wind_speed: Double,
    val wind_deg: Int,
    val weather: List<WeatherDescription>,
    val clouds: Int,
    val pop: Double,
    val rain: Double,
    val uvi: Double,
) : Parcelable


@Parcelize
data class Temperature(
    @PrimaryKey(autoGenerate = true)

    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double,
) : Parcelable


@Parcelize
data class WeatherDescription(
    @PrimaryKey(autoGenerate = true)

    val id: Int,
    val main: String,
    val description: String,
    val icon: String
) : Parcelable


@Parcelize
data class Alert(
    @PrimaryKey(autoGenerate = true)

    val sender_name: String,
    val event: String,
    val start: Long,
    val end: Long,
    val description: String,
    val tags: List<String>,
) : Parcelable

