package com.github.tumusx.camlocation.views

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Telephony.Sms
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.tumusx.camlocation.R
import com.github.tumusx.idle.alarmManager.IdleAlarmManager
import com.github.tumusx.tasks.broadcast.SmsBroadcastReceiver
import com.github.tumusx.tasks.listener.States
import com.github.tumusx.tasks.util.TypeError
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient

class MainActivity : AppCompatActivity() {
    private lateinit var smsClient: SmsRetrieverClient

    private val broadcastReceiver = SmsBroadcastReceiver()
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resultLauncher()
        smsClient = SmsRetriever.getClient(this@MainActivity)
        val idleAlarmManager = IdleAlarmManager(this)
        val actionId = IntentFilter()
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(broadcastReceiver, intentFilter)
        configureFieldsWithSMS()
        idleAlarmManager.setAlarmManager()
        initSmsClient()
    }

    fun initSmsClient() {
        smsClient.startSmsUserConsent(null).addOnSuccessListener {
            Toast.makeText(this, "Esperando pelo SMS", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
            it.printStackTrace()
        }
    }
    private fun configureFieldsWithSMS() {
        States.instanceStates(object : States {
            override fun onSuccess(consentIntent: Intent) {
                resultLauncher.launch(consentIntent)
            }

            override fun error(typeError: TypeError) {
                Log.d("ERROR", typeError.name)
            }
        })
    }

    private fun resultLauncher() {
         resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result->
            if(result.resultCode == Activity.RESULT_OK) {
                val sms = result.data?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE).toString()
                Toast.makeText(this@MainActivity, sms, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }
}