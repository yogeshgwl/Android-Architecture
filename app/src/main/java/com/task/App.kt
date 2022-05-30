package com.task

import android.app.Application
import android.provider.Settings
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class App : Application() {
    init {
        // We are adding this userId/userType in analytics this will help to sort analytics data
        // On user session start, set these userId/userType details either from shared preference/datastore or any used database
        userId = "userId"                                                                           // if user is not yet in session then use device id
        userType = "userType"                                                                       // If our app have multiple user types then we can use this
    }

    companion object {
        lateinit var userId: String
        lateinit var userType: String
    }

    override fun onCreate() {
        super.onCreate()
        userId = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
    }
}
