package com.example.weather.utility.workmanager

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.impl.utils.ForceStopRunnable.BroadcastReceiver

@SuppressLint("RestrictedApi")
class AlertBroadcast: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        AlarmHelper(context,"","")
        Log.d("WeatherAlertWorker", "onReceive")

    }
}