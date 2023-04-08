package com.github.tumusx.tasks.listener

import com.github.tumusx.tasks.util.TypeError

interface States {

    fun onSuccess(sms: String?)
    fun error(typeError: TypeError)

    companion object {
        var statesResult: States? = null
        fun instanceStates(states: States) {
            statesResult = states
        }
    }
}