package com.github.tumusx.idle.alarmManager

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.github.tumusx.idle.R
import java.util.*

class IdleAlarmManager(context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val alarmIntent =
        PendingIntent.getBroadcast(context, 0, Intent(context, AlarmReceiver::class.java), 0)
    private val calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, 16)
        set(Calendar.MINUTE, 43)
    }

    fun setAlarmManager() = alarmManager.setExactAndAllowWhileIdle (
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        alarmIntent
    )
}

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val builder = NotificationCompat.Builder(context, "12345")
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("Murillo")
                .setContentText("Entre no nosso aplicativo")
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("Entre no aplicativo para realizar suas tarefas...")
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            val name = "notificacao"
            val description = "descricao"
            val channelNotification = NotificationChannel(
                "12345",
                name,
                NotificationManager.IMPORTANCE_HIGH,
            ).also {
                it.description = description
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channelNotification)
            notificationManager.notify(12345, builder.build())
        }
    }
}