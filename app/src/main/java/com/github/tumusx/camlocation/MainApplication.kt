package com.github.tumusx.camlocation

import android.app.Application
import com.github.tumusx.idle.alarmManager.IdleAlarmManager

class MainApplication : Application() {
    override fun onCreate() {
        IdleAlarmManager(this)
        super.onCreate()
    }
}