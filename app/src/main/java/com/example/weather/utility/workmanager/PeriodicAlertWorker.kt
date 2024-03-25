package com.example.weather.utility.workmanager

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weather.model.localDatabase.ConcreteLocalSource
import com.example.weather.model.pojos.Alert
import com.example.weather.model.pojos.UserWeatherAlert
import com.example.weather.model.remoteDatabase.WeatherClient
import com.example.weather.model.repository.RepoInterface
import com.example.weather.model.repository.Repository
import com.example.weather.model.sharedPrefrence.SharedPrefrences
import com.example.weather.utility.helper.Constants
import com.example.weather.utility.networkconnection.NetworkStateManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*

class PeriodicAlertWorker (

    appContext: Context,
    workerParams: WorkerParameters,
    ) : CoroutineWorker(appContext, workerParams) {

    var repo: RepoInterface = Repository.getInstance(
        WeatherClient.getInstance(),
        ConcreteLocalSource.getInstance(appContext)
    )
    var sharedPreferences: SharedPrefrences = SharedPrefrences(appContext)
    val networkStateManager: NetworkStateManager = NetworkStateManager(appContext)


    val notification = sharedPreferences.getString(
        Constants.NOTIFICATIONS,
        Constants.ENUM_NOTIFICATIONS.Enabled.toString()
    )
    val alertType = sharedPreferences.getString(
        Constants.ALERT_TYPE,
        Constants.Enum_ALERT.NOTIFICATION.toString()
    )


    override suspend fun doWork(): Result {
        return try {
            val userWeatherAlerts = parseUserWeatherAlerts(inputData.getString("UserWeatherAlert"))
            val weatherAlerts = parseWeatherAlerts(inputData.getString("WeatherAlerts"))

            if (weatherAlerts.isNullOrEmpty()) {
                Log.d("WeatherAlertWorker", "weatherAlerts is empty ? ${weatherAlerts.isNullOrEmpty()} ")

                if (!userWeatherAlerts.isNullOrEmpty()) {
                    val shouldFireAlarm = shouldFireAlarm(userWeatherAlerts[0].timeFrom)
                    if (shouldFireAlarm) {
                        triggerAlarm()
                    }
                }
            } else {
                compareAndTriggerAlarm(userWeatherAlerts, weatherAlerts)
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("WeatherAlertWorker", "Error in doWork: ${e.message}", e)
            Result.failure()
        }
    }

    private fun parseUserWeatherAlerts(jsonString: String?): List<UserWeatherAlert> {
        Log.d("WeatherAlertWorker", " parse userWeatherAlerts ")

        return Gson().fromJson<List<UserWeatherAlert>>(
            jsonString,
            object : TypeToken<List<UserWeatherAlert>>() {}.type
        ) ?: emptyList()

    }

    private fun parseWeatherAlerts(jsonString: String?): List<Alert> {
        Log.d("WeatherAlertWorker", "parse weatherAlerts ")

        return Gson().fromJson<List<Alert>>(
            jsonString,
            object : TypeToken<List<Alert>>() {}.type
        ) ?: emptyList()

    }


    private fun shouldFireAlarm(userStartTime: Long): Boolean {
        val currentTimeMillis = System.currentTimeMillis() /1000
        Log.d("WeatherAlertWorker", "shouldFireAlarm ${currentTimeMillis == userStartTime} ")

        return currentTimeMillis == userStartTime
    }

    private  fun triggerAlarm() {
        val intent = Intent("com.example.myapp.ALARM_ACTION")
        applicationContext.sendBroadcast(intent)
        Log.d("WeatherAlertWorker", "Triggered alarm")

        /*  val desc = " "
          val countryName = sharedPreferences.getString(Constants.COUNTRY_NAME, "")
          countryName?.let {
              // Trigger alarm logic here
          }*/
    }



    private suspend fun compareAndTriggerAlarm(
        userWeatherAlerts: List<UserWeatherAlert>,
        weatherAlerts: List<Alert>
    ) {

        var desc = ""
        //triggerAlarm(desc)
        for (userAlert in userWeatherAlerts) {
            for (apiAlert in weatherAlerts) {
                if (isMatch(userAlert, apiAlert)) {
                    triggerAlarm()
                    break // Break loop if a match is found
                }
            }
        }
    }

    private fun isMatch(userAlert: UserWeatherAlert, apiAlert: Alert): Boolean {
        val userStartTime = userAlert.timeFrom
        val userEndTime = userAlert.timeTo
        val apiStartTime = apiAlert.start
        val apiEndTime = apiAlert.end
        Log.d(
            "WeatherAlertWorker",
            " doWork proccesed ${userStartTime} ${userEndTime} ${apiStartTime} ${apiEndTime}"
        )

        if (userStartTime >= apiStartTime && userEndTime <= apiEndTime) {
            return true
        }
        return false
    }


    private suspend fun deleteProcessedAlert(roomAlert: UserWeatherAlert) {
        runBlocking { repo.deleteAlertItem(roomAlert) }
    }


    private fun showNotification(roomAlert: UserWeatherAlert, desc: String) {
        val countryName = sharedPreferences.getString(Constants.COUNTRY_NAME, "")
        countryName?.let { NotificationHelper.sendNotification(applicationContext, desc) }
    }
}




/*private fun shouldFireAlarm(userStartTime: Long): Boolean {
       val currentTimeMillis = System.currentTimeMillis()
       return currentTimeMillis >= userStartTime
   }
*/
/* private suspend fun triggerAlarm(desc: String) {

     withContext(Dispatchers.Main) {
         val countryName = sharedPreferences.getString(Constants.COUNTRY_NAME, "")
         //   countryName?.let { AlarmHelper(applicationContext, desc, it).onCreate() }
         Log.d("WeatherAlertWorker", " triggerAlarm ${countryName} ")

     }
 }*/

/*  private fun cancelPeriodicWork(roomAlert: UserWeatherAlert) {
      WorkManager.getInstance(applicationContext)
          .cancelAllWorkByTag( roomAlert.endDate.toString())
  }*/

/* private suspend fun deleteAlertAndCancelWork(roomAlert: UserWeatherAlert) {
    repo.deleteAlertItem(roomAlert)
    WorkManager.getInstance(applicationContext)
        .cancelAllWorkByTag(roomAlert.endDate.toString())
}
private fun shouldTriggerAlert(userAlert: UserWeatherAlert, weatherAlert: Alert): Boolean {
    val currentTimeMillis = System.currentTimeMillis()
    val isToday = isAlertToday(userAlert, weatherAlert)
    val isTime = checkTime(userAlert, currentTimeMillis)
    Log.d("WeatherAlertWorker", "shouldTriggerAlert: roomAlert is null" + isToday + isTime)

    return isToday && isTime
}

private fun isAlertToday(userAlert: UserWeatherAlert, weatherAlert: Alert): Boolean {
    val weatherAlertStart = weatherAlert.start * 1000L // Convert seconds to milliseconds
    val weatherAlertEnd = weatherAlert.end * 1000L // Convert seconds to milliseconds

    val userAlertStart = userAlert.timeFrom
    val userAlertEnd = userAlert.timeTo
    Log.d("WeatherAlertWorker", "isAlertToday:weatherAlertStart " + weatherAlertStart )
    Log.d("WeatherAlertWorker", "isAlertToday:weatherAlertEnd " + weatherAlertEnd )
    Log.d("WeatherAlertWorker", "isAlertToday:userAlertStart " + userAlertStart )
    Log.d("WeatherAlertWorker", "isAlertToday:userAlertEnd " + userAlertEnd )

    // Check if the weather alert start and end fall within the user's observation time
    return (weatherAlertStart >= userAlertStart && weatherAlertStart <= userAlertEnd) ||
            (weatherAlertEnd >= userAlertStart && weatherAlertEnd <= userAlertEnd)
}

private fun checkTime(userAlert: UserWeatherAlert, currentTimeMillis: Long): Boolean {
    val userAlertStart = userAlert.timeFrom
    val userAlertEnd = userAlert.timeTo
    Log.d("WeatherAlertWorker", "checkTime: userAlertStarting and ending " + userAlert.timeFrom + userAlert.timeTo)

    // Check if the current time falls within the user's observation time window
    return currentTimeMillis >= userAlertStart && currentTimeMillis <= userAlertEnd
}*/
/* private suspend fun fetchWeatherData(): ApiResponse? {
      return try {
          if (networkStateManager.isConnected()) {

              repo.getApiCallResponse(lat,lon,units,language)
          } else {
              null
          }
      } catch (e: Exception) {
          Log.e("WeatherAlertWorker", "Error fetching weather data: ${e.message}")
          null
      }
  }*/

   /* private fun shouldTriggerAlert(alert: UserWeatherAlert): Boolean {
       // val currentDateInMillis = Constants.convertDateToLong(convertLongToDayDateAlert(Date().time))
        val currentTimeMillis = Constants.convertTimeToLong(convertLongToTime(Calendar.getInstance().time.time))
        val isToday = isAlertToday(alert)
        val isTime = checkTime(alert, currentTimeMillis)

        return isToday && isTime
    }

    private fun isAlertToday(alert: UserWeatherAlert): Boolean {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val date = "$day/${month + 1}/$year"
        val currentDay = convertDateToLong(date)
        return currentDay  <= alert.endDate
    }

    private fun checkTime(alert: UserWeatherAlert, currentTimeMillis: Long): Boolean {
        val alertTimeMillis = alert.timeFrom
        val thresholdMillis = 300000L
        return currentTimeMillis >= alertTimeMillis && currentTimeMillis <= alertTimeMillis + thresholdMillis
    }*/

   /* private fun getTimeDifference(alert: UserWeatherAlert): Long {
        val hour = TimeUnit.HOURS.toMillis(Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toLong())
        val minute = TimeUnit.MINUTES.toMillis(Calendar.getInstance().get(Calendar.MINUTE).toLong())
        return alert.timeFrom - (hour + minute)
    }*/

/*

    val lat: Double? = sharedPreferences.getString(Constants.GPS_LAT, "0.0")?.toDouble()
    val lon: Double? = sharedPreferences.getString(Constants.GPS_LON, "0.0")?.toDouble()
    val language: String? = sharedPreferences.getString(Constants.LANGUAGE, "en")
    val units: String? = sharedPreferences.getString(Constants.SPEED_UNIT, Constants.ENUM_UNITS.standard.toString())
*/

    // Check if roomAlert is null
    /*  if (userWeatherAlert == null || weatherAlerts == null) {
          Log.e("WeatherAlertWorker", "Error in doWork: roomAlert is null" + Result.failure())
          return Result.failure()
      }
      for (alert in weatherAlerts) {
          if (shouldTriggerAlert(userWeatherAlert, alert)) {
          }

          //val apiCallResponse = fetchWeatherData()
          val desc = weatherAlerts?.get(0)?.event
              ?: applicationContext.getString(R.string.no_alert_weather_is_fine)
          /// }


          if (notification == Constants.ENUM_NOTIFICATIONS.Enabled.toString()) {
              if (alertType == Constants.Enum_ALERT.NOTIFICATION.toString()) {
                  showNotification(userWeatherAlert, desc)
              } else {
                  triggerAlarm(userWeatherAlert, desc)
              }

              deleteProcessedAlert(userWeatherAlert)
          }
      else {
          deleteAlertAndCancelWork(userWeatherAlert)
      }
  }*/
    /*
            val alertWorker = inputData.getString("AlertWorker")
            val userAlert = Gson().fromJson(alertWorker, UserWeatherAlert::class.java)
*/
    /*   val userWeatherAlert =
              Gson().fromJson(userWeatherAlertJsonString, UserWeatherAlert::class.java)
          val weatherAlerts =
              Gson().fromJson(weatherAlertsJsonString, Array<Alert>::class.java).toList()*/
/* override suspend fun doWork(): Result {
       return withContext(Dispatchers.Main) {
           try {
               val userWeatherAlertJsonString = inputData.getString("UserWeatherAlert")
               val weatherAlertsJsonString = inputData.getString("WeatherAlerts")

               Log.d(
                   "WeatherAlertWorker",
                   "userWeatherAlertJsonString: $userWeatherAlertJsonString"
               )
               Log.d("WeatherAlertWorker", "weatherAlertsJsonString: $weatherAlertsJsonString")

               val userWeatherAlerts = Gson().fromJson<List<UserWeatherAlert>>(
                   userWeatherAlertJsonString,
                   object : TypeToken<List<UserWeatherAlert>>() {}.type
               )
               val weatherAlerts = Gson().fromJson<List<Alert>>(
                   weatherAlertsJsonString,
                   object : TypeToken<List<Alert>>() {}.type
               )
               Log.d("WeatherAlertWorker", "userWeatherAlerts size: ${userWeatherAlerts?.size}")
               Log.d("WeatherAlertWorker", "weatherAlerts size: ${weatherAlerts?.size}")

               if (weatherAlerts.isNullOrEmpty()) {
                   if (!userWeatherAlerts.isNullOrEmpty()) {
                       // val desc = weatherAlerts[0].description
                       val desc = " "
                       val countryName = sharedPreferences.getString(Constants.COUNTRY_NAME, "")
                       if (shouldFireAlarm(userWeatherAlerts[0].timeFrom)) {

                           val intent = Intent("com.example.myapp.ALARM_ACTION")
// You can add any extra data to the intent if needed
                           applicationContext.sendBroadcast(intent)
                           countryName?.let {
                               *//*AlarmHelper(
                                    applicationContext as Application,
                                    desc,
                                    it
                                ).onCreate()*//*
                            }
                        }
                    }
                } else {
                    // Compare user weather alerts with API weather alerts and trigger an alarm if necessary
                    compareAndTriggerAlarm(userWeatherAlerts, weatherAlerts)
                    Log.d("WeatherAlertWorker", " doWork proccesed" + Result.success())
                }
                Result.success()
            } catch (e: Exception) {
                Log.e("WeatherAlertWorker", "Error in doWork: ${e.message}")
                e.printStackTrace() // Log stack trace for debugging purposes
                Result.failure()
            }
        }
    }*/
