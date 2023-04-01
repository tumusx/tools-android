package com.github.tumusx.idle.alarmManager

import android.Manifest
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.Calendar

class IdleAlarmManager(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setExtractAlarmManager() {
        val timeEvent = convertToAlarmTimeMillis(18, "08".toInt())
        val pendingIntent = createIntentAlarm()
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeEvent, pendingIntent)
    }

    private fun createIntentAlarm() : PendingIntent {
        // 1
        val intent = Intent(context, IdleReceiver::class.java)
        // 2
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        const val REQUEST_CODE = 123
    }
}
fun Calendar.setHourAndMinute(hour: Int, minute: Int) : Calendar {
    set(Calendar.HOUR_OF_DAY, hour)
    set(Calendar.MINUTE, minute)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
    return this
}
fun Long.plusOneDay(): Long = this + 24 * 60 * 60 * 1000

fun convertToAlarmTimeMillis(hours: Int, minute: Int) : Long {
    val calendar = Calendar.getInstance()
    val currentTime = calendar.timeInMillis
    val preposedTime = calendar.setHourAndMinute(hours, minute).timeInMillis
    val alarmTimeMillis = if(preposedTime > currentTime) {
        preposedTime
    }else{
        preposedTime.plusOneDay()
    }
    return alarmTimeMillis
}

class IdleReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent?) {
        try {
            val notification = NotificationCompat.Builder(context, "12345")
                .setContentTitle("Hora de fazer checkout")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentText("Entre no aplicativo para fazer checkout")
                .build().also { notification ->
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    NotificationManagerCompat.from(context)
                        .notify(notification.channelId.toInt(), notification)
                }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}