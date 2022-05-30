package com.task.ui.component.splash

import android.content.Intent
import android.os.Bundle
import com.task.databinding.SplashLayoutBinding
import com.task.ui.base.BaseActivity
import com.task.ui.component.login.LoginActivity
import com.task.SPLASH_DELAY
import com.task.utils.analytics.AppAnalyticsImpl
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Created by AhmedEltaher
 */
@AndroidEntryPoint
class SplashActivity : BaseActivity(){

    private lateinit var binding: SplashLayoutBinding

    override fun initViewBinding() {
        binding = SplashLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        appAnalyticsImpl.logEvents(
            AppAnalyticsImpl.Constants.EVENT_USER_ACTIONS,                                                  // Log event name
            Pair(AppAnalyticsImpl.Constants.EVENT_ACTION, AppAnalyticsImpl.Constants.ACTION_APP_OPENED),    // Can add infinite number of parameters in event action details
            Pair("userName", "Shubham"),                                                                    //....
            Pair("userEmail", "Shubham2@gel.com")                                                           //......
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateToMainScreen()
    }

    override fun observeViewModel() {
    }

    private fun navigateToMainScreen() {
        Executors.newSingleThreadScheduledExecutor().schedule({
            val nextScreenIntent = Intent(this, LoginActivity::class.java)
            startActivity(nextScreenIntent)
            finish()
        }, SPLASH_DELAY.toLong(), TimeUnit.MILLISECONDS)
    }
}
