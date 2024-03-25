package com.example.weather.utility.workmanager

import android.content.Context
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.example.weathear.R
import com.example.weathear.databinding.AlarmDialogRowBinding
import java.io.IOException

class AlarmHelper ( private val context: Context,
                    private val description: String,
                    private val country: String
) {
    private lateinit var customDialog: View
    private lateinit var mediaPlayer: MediaPlayer

  /*  init {

    }*/
  fun onCreate() {
      initializeDialog()
      if (::mediaPlayer.isInitialized) {
          mediaPlayer.start()
      }
  }

    private fun initializeDialog() {
        val inflater = LayoutInflater.from(context)
        customDialog = inflater.inflate(R.layout.alarm_dialog_row, null)
        try {
            val assetManager = context.assets
            val assetFileDescriptor = assetManager.openFd("ringtone.mp3")
            mediaPlayer = MediaPlayer().apply {
                setDataSource(
                    assetFileDescriptor.fileDescriptor,
                    assetFileDescriptor.startOffset,
                    assetFileDescriptor.length
                )
                prepare()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("WeatherAlarmHelper", "mediaPlayer catch exception" +  e.message )
        }
        val binding = AlarmDialogRowBinding.bind(customDialog)
        binding.apply {
            txtAlarmDesc.text = description
            txtAlarmCountry.text = country
            btnOkAlarm.setOnClickListener { close() }
        }
        Log.d("WeatherAlarmHelper", "initializeDialog" + country)
    }

    private fun showAlarmDialog() {
        val layoutFlag = getLayoutFlag()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutParams = getLayoutParams(layoutFlag)
        windowManager.addView(customDialog, layoutParams)
        Log.d("WeatherAlarmHelper", "showAlarmDialog")

    }

    private fun getLayoutFlag(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            Log.d("WeatherAlarmHelper", "getLayoutFlag")
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
    }

    private fun getLayoutParams(layoutFlag: Int): WindowManager.LayoutParams {
        val width = (context.resources.displayMetrics.widthPixels * 0.85).toInt()
        return WindowManager.LayoutParams(
            width,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE,
            PixelFormat.TRANSLUCENT
        )
    }

    private fun close() {
        try {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.removeView(customDialog)
            mediaPlayer.release()
            Log.e("WeatherWorkManager", "close")

        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
    }
}
