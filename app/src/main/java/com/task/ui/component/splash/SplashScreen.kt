package com.task.ui.component.splash

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.task.ui.theme.SplashBackground
import com.task.ui.theme.SplashIcon
import kotlinx.coroutines.delay

/**
 * This file represent the Splash screen for lower then android12
 */
const val LaunchDelay = 4000L
const val AnimationDelay = 3000

@Composable
fun SplashScreen(
    navigateToLogin: (String) -> Unit,
) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = AnimationDelay
        )
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(LaunchDelay)
        navigateToLogin
    }
    Splash(alpha = alphaAnim.value)
}

@Composable
fun Splash(alpha: Float) {
    Box(
        modifier = Modifier
            .background(SplashBackground())
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SplashIcon(alpha = alpha)
    }
}

@Composable
@Preview
fun SplashScreenPreview() {
    Splash(alpha = 1f)
}

@Composable
@Preview(uiMode = UI_MODE_NIGHT_YES)
fun SplashScreenDarkPreview() {
    Splash(alpha = 1f)
}