package com.task.utils.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.task.App
import javax.inject.Inject

class AppAnalytics @Inject constructor(val context: Context) {

    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    fun logEvents(logEventName: String, vararg eventActionPairs: Pair<String, Any>) {
        val bundle = Bundle()

        eventActionPairs.forEach { bundle.putValue(it) }

        firebaseAnalytics.apply {
            setAnalyticsCollectionEnabled(true)

            setUserId(App.userId)
            setUserProperty(Constants.USER_TYPE, App.userType)
            logEvent(logEventName, bundle)
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

    class Constants {
        companion object {
            // Properties
            const val USER_TYPE = "user_type"

            // Events
            const val EVENT_USER_ACTIONS = "user_actions"
            const val EVENT_ACTION = "action"
            const val EVENT_RESULT = "result"
            const val EVENT_LOGIN = "user_login"

            // Actions
            const val ACTION_APP_OPENED = "app_opened"
            const val ACTION_LOGIN_SUCCESS = "success"
            const val ACTION_LOGIN_FAIL = "fail"
        }
    }
}