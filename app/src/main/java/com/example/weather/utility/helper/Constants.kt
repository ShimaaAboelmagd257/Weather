package com.example.weather.utility.helper

import android.content.Context
import android.content.res.Configuration
import android.location.Geocoder
import com.example.weathear.R
import com.example.weather.model.sharedPrefrence.SharedPrefrences
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object Constants {

    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val API_KEY = "cad8edbd7bf4c844f7347d7fa46298b3"
    //AIzaSyDMJesp8_yLq331n1JgksC8ivAmkjCx6TU
    const val SPEED_UNIT = "speedUnit"
    const val TEMP_UNIT = "TempUnit"

    const val METRIC = "metric"
    const val IMPERIAL = "imperial"
    const val STANDARD = "standard"
    const val LANGUAGE = "language"
    const val LAT = "lat"
    const val LON = "lon"
    enum class ENUM_LOCATION(){Map,Gps,Fav}
    const val NOTIFICATIONS = "NOTIFICATIONS"
    enum class ENUM_NOTIFICATIONS(){Enabled,Disabled}
    const val LOCATION = "LOCATION"
    const val SHARED_PREFERENCE_NAME="SetupSharedPreferences"
    const val IsMap = "map"
    enum class ENUM_UNITS(){standard,metric,imperial}
    enum class Enum_lANGUAGE(){ar,en}
    enum class Enum_ALERT(){ALARM,NOTIFICATION}
    const val GPS_LON="GPS_LON"
    const val GPS_LAT="GPS_LAT"
    const val MAP_LONH="MAP_LONH"
    const val MAP_LATH="MAP_LATH"
    const val FAV_LON = "Fav_Lat"
    const val FAV_Lat ="FAV_Lat"
    const val COUNTRY_NAME="CountryName"
    const val ALERT_TYPE="ALERT_TYPE"
    const val NOTIFICATION_NAME = "Weather"
    const val NOTIFICATION_CHANNEL = "Weather_channel_01"


    fun getAddress(context: Context, lat: Double, lon: Double): String {
        var address = " "
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addressList = geocoder.getFromLocation(lat, lon, 1)
            if (addressList != null) {
                if (addressList.isNotEmpty()) {
                    address = "${addressList[0].adminArea}, ${addressList[0].countryName}"
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return address
    }

    fun Context.setLocale(language: String) {
        val myLocale = Locale(language)
        Locale.setDefault(myLocale)

        resources.updateConfiguration(Configuration().apply {
            locale = myLocale
            setLayoutDirection(myLocale)
        }, resources.displayMetrics)
    }


    fun convertLongToDayName(time: Long): String {
        val format = SimpleDateFormat("EEEE", Locale.getDefault())
        return format.format(Date(time * 1000))
    }

    fun convertLongToDayDate(time: Long): String {
        val format = SimpleDateFormat("d MMM, yyyy", Locale.getDefault())
        return format.format(time * 1000)
    }

    fun convertDateToLong(date: String): Long {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date: Date = format.parse(date)
        return date.time
    }

    fun convertLongToDayDateAlert(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.format(date)
    }
    fun convertLongToTime(time: Long): String {
        val format = SimpleDateFormat("hh:mm aa", Locale.getDefault())
        return format.format(Date(time * 1000))
    }
    fun convertTimeToLong(time:String):Long{
        val format = SimpleDateFormat("hh:mm a")
        return format.parse(time).time
    }
   /* fun convertLongToTimePicker(time: Long): String {
        val date = Date(time - 7200000)
        val format = SimpleDateFormat("h:mm aa", Locale.getDefault())
        return format.format(date)
    }*/
   fun convertLongToTimePicker(time: Long): String {
       val date = Date(time * 1000) // Convert seconds to milliseconds
       val format = SimpleDateFormat("h:mm aa", Locale.getDefault())
       return format.format(date)
   }

    fun getSpeedUnit(context: Context): String {
        val sharedPreference = SharedPrefrences(context)
        return when (sharedPreference.getString(SPEED_UNIT, METRIC)) {
            IMPERIAL -> {
                context.getString(R.string.m_h)
            }
            else -> {
                context.getString(R.string.m_s)
            }
        }
    }
    fun getTemperatureUnit(context: Context): String {
        val sharedPreference = SharedPrefrences(context)
        return when (sharedPreference.getString(TEMP_UNIT, METRIC)) {
            IMPERIAL -> {
                context.getString(R.string.f)
            }
            STANDARD -> {
                context.getString(R.string.k)
            }
            else -> {
                context.getString(R.string.c)
            }
        }
    }

}