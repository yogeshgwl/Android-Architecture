package com.task.ui.component.login

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.animation.doOnEnd
import com.task.ui.ApplicationApp
import com.task.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    lateinit var content: View

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            setContent {
                ApplicationApp(windowSizeClass = calculateWindowSizeClass(activity = this))
            }
        }
        // To load the login screen we used delay ro 2000.
        GlobalScope.launch {
            delay(LaunchToLogin)
            showSplashScreen()
        }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
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
                AppTheme {
                    ApplicationApp(windowSizeClass = calculateWindowSizeClass(activity = this))
                }
            }
        }
    }

    companion object {
        const val LaunchToLogin = 2000L
    }
}
