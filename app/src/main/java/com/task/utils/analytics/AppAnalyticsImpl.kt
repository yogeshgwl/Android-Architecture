package com.task.utils.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.task.App
import javax.inject.Inject

class AppAnalyticsImpl @Inject constructor(val context: Context): AppAnalytics {

    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    override fun logEvents(logEventName: String, eventActionPairs: HashMap<String, Any>) {
        val bundle = Bundle()

        eventActionPairs.forEach { (key, value) ->
            bundle.putValue(key, value)
        }
        firebaseAnalytics.apply {
            setAnalyticsCollectionEnabled(true)

            setUserId(App.userId)
            setUserProperty(Constants.USER_TYPE, App.userType)
            logEvent(logEventName, bundle)
        }
    }

    private fun Bundle.putValue(key: String, value: Any) {
        when (value) {
            is Int -> putInt(key, value)
            is Float -> putFloat(key, value)
            is Long -> putLong(key, value)
            is Double -> putDouble(key, value)
            is String -> putString(key, value)
            is Boolean -> putBoolean(key, value)
            else -> putString(key, value.toString())
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