package com.github.tumusx.tasks.broadcast

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.tumusx.tasks.listener.States
import com.github.tumusx.tasks.util.TypeError
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SmsBroadcastReceiver(private val activity: AppCompatActivity) : BroadcastReceiver() {

    private fun <T>configureParcelable(parcelable: String, intent: Intent, classParcelable: Class<T>) : T? {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelable(parcelable, classParcelable)
        }else{
            intent.extras?.getParcelable(parcelable)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == SmsRetriever.SMS_RETRIEVED_ACTION) {
            try {
                val smsRetrieverStatus = configureParcelable(SmsRetriever.EXTRA_STATUS, intent, Status::class.java)

                when (smsRetrieverStatus?.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        val consentIntent = configureParcelable(SmsRetriever.EXTRA_CONSENT_INTENT, intent, Intent::class.java)
                        val resultLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result->
                            if(result.resultCode == Activity.RESULT_OK) {
                                States.statesResult?.onSuccess(result.data?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE))
                            }
                        }
                        resultLauncher.launch(consentIntent)
                    }
                    CommonStatusCodes.ERROR -> {
                        States.statesResult?.error(TypeError.ERROR)
                    }

                    CommonStatusCodes.TIMEOUT -> {
                        States.statesResult?.error(TypeError.TIMEOUT)
                    }
                }

            } catch (exception: java.lang.Exception) {
                States.statesResult?.error(TypeError.EXCEPTION)
                exception.printStackTrace()
            }
        }
    }
}