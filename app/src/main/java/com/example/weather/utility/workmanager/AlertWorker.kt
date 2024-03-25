package com.example.weather.utility.workmanager

// WorkManagerUtility.kt

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.weather.model.pojos.Alert
import com.example.weather.model.pojos.UserWeatherAlert
import com.google.gson.Gson
import java.util.concurrent.TimeUnit

object AlertWorker {

    /*interface WorkEnqueueCallback {
        fun onWorkEnqueued()
        fun onWorkEnqueueError(errorMessage: String)
    }*/

    fun setAlertWorkManager(context: Context, userWeatherAlert: List<UserWeatherAlert> , weatherAlerts: List<Alert>? ) {
        try {
            // Serialize the UserWeatherAlert object to JSON string
            val userWeatherAlertJsonString = Gson().toJson(userWeatherAlert)
            val weatherAlertsJsonString = Gson().toJson(weatherAlerts)

            val inputData = Data.Builder()
                .putString("UserWeatherAlert", userWeatherAlertJsonString)
                .putString("WeatherAlerts", weatherAlertsJsonString)
                .build()



            // Define constraints for the periodic work (if needed)
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build()

            // Create the periodic work request with input data and constraints
            val periodicWorkRequest =
                PeriodicWorkRequestBuilder<PeriodicAlertWorker>(15, TimeUnit.SECONDS)
                    .setInputData(inputData)
                    .setConstraints(constraints) // Optional: set constraints if needed
                    .build()

            // Enqueue the periodic work request
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "daily",
                ExistingPeriodicWorkPolicy.REPLACE,
                periodicWorkRequest
            )
          //  callback.onWorkEnqueued()

            Log.e("WeatherWorkManager", "setAlertWorkManager: Periodic work request enqueued successfully")
        } catch (e: Exception) {
           // callback.onWorkEnqueueError(e.message ?:" ")
            Log.e("WeatherWorkManager", "setAlertWorkManager: Error - ${e.message}")
        }
    }
}

