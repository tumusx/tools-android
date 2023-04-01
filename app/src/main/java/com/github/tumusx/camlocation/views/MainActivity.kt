package com.github.tumusx.camlocation.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.tumusx.camlocation.R
import com.github.tumusx.idle.alarmManager.IdleAlarmManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        IdleAlarmManager(this).setExtractAlarmManager()
    }
}