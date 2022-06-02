package com.task.ui.component.login

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.lifecycle.LiveData
import androidx.navigation.compose.rememberNavController
import com.google.android.material.snackbar.Snackbar
import com.task.data.Resource
import com.task.data.dto.login.LoginResponse
import com.task.databinding.LoginActivityBinding
import com.task.navigation.SetupNavGraph
import com.task.ui.base.BaseActivity
import com.task.ui.component.dashboard.DashboardActivity
import com.task.ui.theme.SplashScreenTheme
import com.task.utils.SingleEvent
import com.task.utils.analytics.AppAnalyticsImpl
import com.task.utils.observe
import com.task.utils.setupSnackbar
import com.task.utils.showToast
import com.task.utils.toGone
import com.task.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var binding: LoginActivityBinding
    lateinit var content: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showSplashScreen()
        binding.login.setOnClickListener { doLogin() }
    }

    private fun showSplashScreen() {
        content = findViewById(android.R.id.content)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            content.viewTreeObserver.addOnPreDrawListener(object :
                ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean =
                    when {
                        loginViewModel.mockDataLoading() -> {
                            content.viewTreeObserver.removeOnPreDrawListener(this)
                            true
                        }
                        else -> false
                    }
            })

            // custom exit on splashScreen
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                // custom animation.
                ObjectAnimator.ofFloat(
                    splashScreenView,
                    View.TRANSLATION_X,
                    0f,
                    splashScreenView.width.toFloat()
                ).apply {
                    duration = 1000
                    // Call SplashScreenView.remove at the end of your custom animation.
                    doOnEnd {
                        splashScreenView.remove()
                    }
                }.also {
                    // Run your animation.
                    it.start()
                }
            }
        } else {
            setContent {
                SplashScreenTheme {
                    val navController = rememberNavController()
                    SetupNavGraph(navController = navController)
                }
            }
        }
    }

    override fun initViewBinding() {
        binding = LoginActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun observeViewModel() {
        observe(loginViewModel.loginLiveData, ::handleLoginResult)
        observeSnackBarMessages(loginViewModel.showSnackBar)
        observeToast(loginViewModel.showToast)
    }

    private fun doLogin() {
        loginViewModel.doLogin(
            binding.username.text.trim().toString(),
            binding.password.text.toString()
        )
    }

    private fun handleLoginResult(status: Resource<LoginResponse>) {
        when (status) {
            is Resource.Loading -> binding.loaderView.toVisible()
            is Resource.Success -> status.data?.let {
                binding.loaderView.toGone()
                loginViewModel.appAnalyticsImpl.logEvents(
                    AppAnalyticsImpl.Constants.EVENT_LOGIN,
                    hashMapOf(
                        AppAnalyticsImpl.Constants.EVENT_RESULT to AppAnalyticsImpl.Constants.ACTION_LOGIN_SUCCESS
                    )
                )
                navigateToMainScreen()
            }
            is Resource.DataError -> {
                binding.loaderView.toGone()
                status.errorCode?.let {
                    loginViewModel.showToastMessage(it)
                    loginViewModel.appAnalyticsImpl.logEvents(
                        AppAnalyticsImpl.Constants.EVENT_LOGIN,
                        hashMapOf(
                            AppAnalyticsImpl.Constants.EVENT_RESULT to AppAnalyticsImpl.Constants.ACTION_LOGIN_FAIL
                        )
                    )
                }
            }
        }
    }

    private fun navigateToMainScreen() {
        val nextScreenIntent = Intent(this, DashboardActivity::class.java)
        startActivity(nextScreenIntent)
        finish()
    }

    private fun observeSnackBarMessages(event: LiveData<SingleEvent<Any>>) {
        binding.root.setupSnackbar(this, event, Snackbar.LENGTH_LONG)
    }

    private fun observeToast(event: LiveData<SingleEvent<Any>>) {
        binding.root.showToast(this, event, Snackbar.LENGTH_LONG)
    }
}
