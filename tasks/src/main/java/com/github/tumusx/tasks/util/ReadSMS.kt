package com.github.tumusx.tasks.util

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.phone.SmsRetriever


fun AppCompatActivity.receiveSmsClient() {
    val task = SmsRetriever.getClient(this).startSmsUserConsent(null)
}