package com.task.ui.component.splash

import android.content.Intent
import android.os.Bundle
import com.task.databinding.SplashLayoutBinding
import com.task.ui.base.BaseActivity
import com.task.ui.component.login.LoginActivity
import com.task.SPLASH_DELAY
import com.task.utils.analytics.AppAnalytics
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by AhmedEltaher
 */
@AndroidEntryPoint
class SplashActivity : BaseActivity(){

    @Inject lateinit var appAnalytics: AppAnalytics

    private lateinit var binding: SplashLayoutBinding

    override fun initViewBinding() {
        binding = SplashLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        appAnalytics.logEvents(Pair(AppAnalytics.Constants.Event_ACTION, AppAnalytics.Constants.Action_APP_OPENED), Pair("userName", "Shubham"), Pair("userName2", "Shubham2"))
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
