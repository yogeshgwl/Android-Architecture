/**
 * Name: Login.kt
 * Created by: Nitin 20 Jun 2022
 * Copyright © 2022 GWL INC. All rights reserved.
 * Purpose: This file represent the Login screen
 */

package com.task.ui.component.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.task.R
import com.task.data.Resource
import com.task.data.dto.login.LoginResponse
import com.task.exceptions.AppExceptions
import com.task.extensions.showSnackbar
import com.task.ui.component.custom.AppGradientBackground
import com.task.ui.theme.size_10
import com.task.ui.theme.size_2
import com.task.ui.theme.size_6
import com.task.ui.theme.size_8
import com.task.utils.CustomTextField
import com.task.utils.FocusedTextFieldKey
import com.task.utils.ScreenEvent
import com.task.utils.analytics.AppAnalyticsImpl
import com.task.utils.logs.AppLogger
import com.task.utils.logs.getClassTag
import java.io.FileNotFoundException

/**
 * This file represent the Login screen
 */

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Login(
    navigateToDashboard: () -> Unit,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val emailAddressFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController =
        LocalSoftwareKeyboardController.current    //It is software keyboard controller and it is Experimental compose UIApi
    val events = remember(viewModel.events, lifecycleOwner) {
        viewModel.events.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }

    /**
     * collect event for Focus handling
     */
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is ScreenEvent.ShowToast -> {
                    scope.showSnackbar(
                        snackbarHostState = snackbarHostState,
                        message = context.getString(event.messageId)
                    )
                }
                is ScreenEvent.UpdateKeyboard -> {
                    if (event.show) keyboardController?.show() else keyboardController?.hide()
                }
                is ScreenEvent.ClearFocus -> focusManager.clearFocus()
                is ScreenEvent.RequestFocus -> {
                    if (event.textFieldKey == FocusedTextFieldKey.EmailAddress)
                        emailAddressFocusRequester.requestFocus()
                    else
                        passwordFocusRequester.requestFocus()
                }
                is ScreenEvent.MoveFocus -> focusManager.moveFocus(event.direction)
            }
        }
    }

    // Subscribe to observables
    viewModel.loginLiveData.observe(lifecycleOwner) {
        handleLoginResult(navigateToDashboard, it, viewModel)
    }
    viewModel.showToast.observe(lifecycleOwner) {
        scope.showSnackbar(
            snackbarHostState = snackbarHostState,
            message = it.peekContent().toString()
        )
    }
    AppGradientBackground {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            content = { innerPadding ->
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    constraintSet = ConstraintSet {
                        val email = createRefFor("email")
                        val pass = createRefFor("pass")
                        val button = createRefFor("button")
                        val progress = createRefFor("progress")

                        constrain(email) {
                            start.linkTo(parent.start)
                        }
                        constrain(pass) {
                            top.linkTo(email.bottom)
                        }
                        constrain(button) {
                            top.linkTo(pass.bottom)
                        }
                        constrain(progress) {
                            centerTo(parent)
                        }
                    }

                ) {
                    EmailTextField(
                        viewModel = viewModel,
                        emailAddressFocusRequester = emailAddressFocusRequester,
                        modifier = Modifier.layoutId("email")
                    )
                    PasswordTextField(
                        viewModel = viewModel,
                        passwordFocusRequester = passwordFocusRequester,
                        modifier = Modifier.layoutId("pass")
                    )
                    LoginButton(
                        viewModel = viewModel,
                        modifier = Modifier.layoutId("button")
                    )
                    val progress by viewModel.progressBar.collectAsState()
                    CircularProgressIndicator(
                        modifier = Modifier
                            .layoutId("progress")
                            .alpha(progress),
                    )
                }
            }
        )
    }
}

@Composable
fun EmailTextField(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel,
    emailAddressFocusRequester: FocusRequester,
) {
    val userName by viewModel.userName.collectAsState()
    CustomTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(size_10)
            .clickable { emailAddressFocusRequester.requestFocus() }
            .focusRequester(emailAddressFocusRequester)
            .onFocusChanged { focusState ->
                viewModel.onTextFieldFocusChanged(
                    key = FocusedTextFieldKey.EmailAddress,
                    isFocused = focusState.isFocused
                )
            }
            .focusable(),
        inputWrapper = userName,
        labelResId = R.string.enter_username,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        ),
        onValueChange = viewModel::onEmailAddressEntered,
        onImeKeyAction = viewModel::onLoginClick,
        trailingIcon = null
    )
}

@Composable
fun PasswordTextField(
    viewModel: LoginViewModel,
    passwordFocusRequester: FocusRequester,
    modifier: Modifier
) {
    val password by viewModel.password.collectAsState()
    val passwordVisibility: Boolean by remember { mutableStateOf(false) }
    CustomTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(size_10)
            .clickable { passwordFocusRequester.requestFocus() }
            .focusRequester(passwordFocusRequester)
            .onFocusChanged { focusState ->
                viewModel.onTextFieldFocusChanged(
                    key = FocusedTextFieldKey.Password,
                    isFocused = focusState.isFocused
                )
            }
            .focusable(),
        labelResId = R.string.password,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        inputWrapper = password,
        onValueChange = viewModel::onPasswordEntered,
        onImeKeyAction = viewModel::onLoginClick,
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = null
    )
}


@Composable
fun LoginButton(
    viewModel: LoginViewModel,
    modifier: Modifier = Modifier,
) {
    val areInputsValid by viewModel.areInputsValid.collectAsState()
    Button(
        onClick = viewModel::onLoginClick,
        enabled = areInputsValid,
        shape = CircleShape,
        // Custom colors for different states
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Blue,
            disabledContainerColor = Color.Gray
                .copy(alpha = 0.2f)
                .compositeOver(Color.Gray)
            // Also contentColor and disabledContentColor
        ),
        // Custom elevation for different states
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = size_8,
            disabledElevation = size_2,
            // Also pressedElevation
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(size_10)
            .testTag("loginButton")

    ) {
        Text(
            text = stringResource(id = R.string.action_sign_in),
            color = Color.White,
            modifier = Modifier.padding(size_6)
        )
    }
}

private fun handleLoginResult(
    navigateToDashboard: () -> Unit,
    status: Resource<LoginResponse>,
    viewModel: LoginViewModel,
) {
    when (status) {
        is Resource.Loading -> {
            viewModel.showHideProgressBar(1f)
        }
        is Resource.Success -> status.data?.let {
            viewModel.showHideProgressBar(0f)
            viewModel.appAnalyticsImpl.logEvents(
                AppAnalyticsImpl.Constants.EVENT_LOGIN,
                hashMapOf(
                    AppAnalyticsImpl.Constants.EVENT_RESULT to AppAnalyticsImpl.Constants.ACTION_LOGIN_SUCCESS
                )
            )
            viewModel.appLogger.printLog(Long.getClassTag(), "Login Success $it", AppLogger.LogType.D)
            navigateToDashboard.invoke()
        }
        is Resource.DataError -> {
            viewModel.showHideProgressBar(0f)
            status.errorCode?.let {
                viewModel.showToastMessage(AppExceptions.getErrorCode(throwable = FileNotFoundException()))
                viewModel.appAnalyticsImpl.logEvents(
                    AppAnalyticsImpl.Constants.EVENT_LOGIN,
                    hashMapOf(
                        AppAnalyticsImpl.Constants.EVENT_RESULT to AppAnalyticsImpl.Constants.ACTION_LOGIN_FAIL
                    )
                )
                viewModel.appLogger.printLog(Long.getClassTag(), "Login Error $it", AppLogger.LogType.E, Throwable(it.toString()))
            }
        }
    }
}


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showSystemUi = true, showBackground = true)
@Preview(name = "phone", device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
@Preview(name = "landscape", device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480")
@Preview(name = "foldable", device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480")
@Preview(name = "tablet", device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480")
@Composable
private fun LoginPreview() {
//    Login(windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(maxWidth, maxHeight)))
}