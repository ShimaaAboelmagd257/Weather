package com.example.weather.utility.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.example.weathear.R
import com.example.weather.weatherMain.MainActivity
import com.example.weather.utility.helper.Constants

object NotificationHelper {

    private const val NOTIFICATION_ID = 0

    fun sendNotification(context: Context, description: String) {
        // Intent for opening the app
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        // Build notification
        val notification = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL)
            .setContentTitle("Weather Status")
            .setContentText(description)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // NotificationManager setup
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Set channel and sound
        notification.setChannelId(Constants.NOTIFICATION_CHANNEL)

        // Create a custom notification channel with sound
        val assetFileDescriptor = context.assets.openFd("ringtone.mp3")
        val ringtoneUri = Uri.parse("content://com.android.externalstorage.documents/document/primary:${assetFileDescriptor.fileDescriptor}")
        val ringtone =
            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/" + "ringtone.mp3")
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()

        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL,
            Constants.NOTIFICATION_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )

        channel.enableLights(true)
        channel.enableVibration(true)
        channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        channel.setSound(ringtoneUri, audioAttributes)

        // Notify using NotificationManager
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }
}
