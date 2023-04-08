package com.github.tumusx.camlocation.views

import android.content.IntentFilter
import android.os.Bundle
import android.provider.Telephony.Sms
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.tumusx.camlocation.R
import com.github.tumusx.idle.alarmManager.IdleAlarmManager
import com.github.tumusx.tasks.broadcast.SmsBroadcastReceiver
import com.github.tumusx.tasks.listener.States
import com.github.tumusx.tasks.util.TypeError

class MainActivity : AppCompatActivity() {
    companion object {
        const val smsRetrievedAction = "com.google.android.gms.auth.api.phone.SMS_RETRIEVED"
    }

    private val broadcastReceiver = SmsBroadcastReceiver(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val idleAlarmManager = IdleAlarmManager(this)
        registerReceiver(broadcastReceiver, IntentFilter(smsRetrievedAction))
        idleAlarmManager.setAlarmManager()
    }

    private fun configureFieldsWithSMS() {
        States.instanceStates(object : States {
            override fun onSuccess(sms: String?) {
                Log.d("SMS", sms.toString())
            }

            override fun error(typeError: TypeError) {
                Log.d("ERROR", typeError.name)
            }
        })
    }

    override fun onDestroy() {
        unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }
}