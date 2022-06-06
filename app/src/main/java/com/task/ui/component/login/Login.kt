package com.task.ui.component.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.task.R
import com.task.data.Resource
import com.task.data.dto.login.LoginResponse
import com.task.extensions.toast
import com.task.ui.theme.size_10
import com.task.ui.theme.size_4
import com.task.utils.CustomTextField
import com.task.utils.FocusedTextFieldKey
import com.task.utils.ScreenEvent
import com.task.utils.analytics.AppAnalyticsImpl

/**
 * This file represent the Login screen
 */


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Login(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel,
) {
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
                is ScreenEvent.ShowToast -> context.toast(event.messageId)
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
        handleLoginResult(navController, it, viewModel)
    }
    viewModel.showToast.observe(lifecycleOwner) {
        context.toast(it.peekContent().toString())
    }

    Surface(
        color = Color.White,
        modifier = modifier
            .padding(size_4)
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize(),
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
            .focusRequester(emailAddressFocusRequester)
            .onFocusChanged { focusState ->
                viewModel.onTextFieldFocusChanged(
                    key = FocusedTextFieldKey.EmailAddress,
                    isFocused = focusState.isFocused
                )
            },
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
            .focusRequester(passwordFocusRequester)
            .onFocusChanged { focusState ->
                viewModel.onTextFieldFocusChanged(
                    key = FocusedTextFieldKey.Password,
                    isFocused = focusState.isFocused
                )
            },
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
fun LoginButton(viewModel: LoginViewModel, modifier: Modifier = Modifier) {
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
            defaultElevation = 8.dp,
            disabledElevation = 2.dp,
            // Also pressedElevation
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)

    ) {
        Text(
            text = stringResource(id = R.string.action_sign_in),
            color = Color.White,
            modifier = Modifier.padding(6.dp)
        )
    }
}

private fun handleLoginResult(
    navController: NavHostController,
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
            navigateToMainScreen(navController)
        }
        is Resource.DataError -> {
            viewModel.showHideProgressBar(0f)
            status.errorCode?.let {
                viewModel.showToastMessage(it)
                viewModel.appAnalyticsImpl.logEvents(
                    AppAnalyticsImpl.Constants.EVENT_LOGIN,
                    hashMapOf(
                        AppAnalyticsImpl.Constants.EVENT_RESULT to AppAnalyticsImpl.Constants.ACTION_LOGIN_FAIL
                    )
                )
            }
        }
    }
}

private fun navigateToMainScreen(navController: NavHostController) {
    TODO("Handle the next screen navigation.")
}

@Composable
@Preview(name = "Login")
private fun LoginPreview() {
    val navController = rememberNavController()
    Login(navController = navController, viewModel = hiltViewModel())
}