package com.task.utils

import android.os.Bundle
import com.task.App

class AppAnalytics {
    companion object {
        // Properties
        private const val USER_TYPE = "user_type"
        
        // Events
        const val Event_ACTION = "action"
        const val Event_LOGIN = "user_login"

        // Actions
        const val Action_APP_OPENED = "app_opened"
        const val Action_LOGIN_SUCCESS = "success"
        const val Action_LOGIN_FAIL = "fail"

        fun logEvents(eventName: String, eventValue: Any, parametersList: ArrayList<Pair<String, Any>>? = null) {
            val bundle = Bundle()

            bundle.putValue(Pair(eventName, eventValue))
            parametersList?.forEach { bundle.putValue(it) }

            App.getFirebaseAnalyticsObject()?.apply {
                setAnalyticsCollectionEnabled(true)

                setUserId(App.userId)
                setUserProperty(USER_TYPE, App.userType)
                logEvent(eventName, bundle)
            }
        }

        private fun Bundle.putValue(value: Pair<String, Any>) {
            when (value.second) {
                is Int -> putInt(value.first, value.second as Int)
                is Float -> putFloat(value.first, value.second as Float)
                is Long -> putLong(value.first, value.second as Long)
                is Double -> putDouble(value.first, value.second as Double)
                is String -> putString(value.first, value.second as String)
                is Boolean -> putBoolean(value.first, value.second as Boolean)
            }
        }
    }
}