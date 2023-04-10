package com.github.tumusx.tasks.listener

import android.content.Intent
import com.github.tumusx.tasks.util.TypeError

interface States {

    fun onSuccess(consentIntent: Intent)
    fun error(typeError: TypeError)

    companion object {
        var statesResult: States? = null
        fun instanceStates(states: States) {
            statesResult = states
        }
    }
}