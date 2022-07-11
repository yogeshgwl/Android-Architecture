package com.task.ui.component.login

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.task.data.Resource
import com.task.data.dto.login.LoginRequest
import com.task.data.dto.login.LoginResponse
import com.task.data.repository.login.UserRepositoryImpl
import com.task.extensions.getStateFlow
import com.task.ui.base.BaseViewModel
import com.task.utils.FocusedTextFieldKey
import com.task.utils.InputValidator
import com.task.utils.InputWrapper
import com.task.utils.ScreenEvent
import com.task.utils.SingleEvent
import com.task.utils.analytics.AppAnalyticsImpl
import com.task.utils.wrapEspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InputErrors(
    val emailErrorId: Int?,
    val passwordErrorId: Int?
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl,
    private val handle: SavedStateHandle
) : BaseViewModel() {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val loginLiveDataPrivate = MutableLiveData<Resource<LoginResponse>>()
    val loginLiveData: LiveData<Resource<LoginResponse>> get() = loginLiveDataPrivate

    /** Error handling as UI **/

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showSnackBarPrivate = MutableLiveData<SingleEvent<Any>>()
    val showSnackBar: LiveData<SingleEvent<Any>> get() = showSnackBarPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast: LiveData<SingleEvent<Any>> get() = showToastPrivate


    val progressBar = handle.getStateFlow(viewModelScope, "progress", 0f)
    val userName = handle.getStateFlow(viewModelScope, "userName", InputWrapper())
    val password = handle.getStateFlow(viewModelScope, "password", InputWrapper())
    val areInputsValid = combine(password, userName) { password, userName ->
        password.value.isNotEmpty() && password.errorId == null &&
                userName.value.isNotEmpty() && userName.errorId == null
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private var focusedTextField = handle["focusedTextField"] ?: FocusedTextFieldKey.EmailAddress
        set(value) {
            field = value
            handle["focusedTextField"] = value
        }
    private val inputEvents = Channel<UserInputEvent>(Channel.CONFLATED)
    private val _events = Channel<ScreenEvent>()
    val events = _events.receiveAsFlow()


    init {
        observeUserInputEvents()
        if (focusedTextField != FocusedTextFieldKey.None) focusOnLastSelectedTextField()
    }

    var dataLoaded: Boolean = false

    fun mockDataLoading(): Boolean {
        viewModelScope.launch {
            delay(5000)
            dataLoaded = true
        }
        return dataLoaded
    }

    fun doLogin(userName: String, passWord: String) {
        Log.d("nitin", "doLogin: Username: $userName   password: $passWord")
        viewModelScope.launch {
            loginLiveDataPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                userRepositoryImpl.doLogin(loginRequest = LoginRequest(userName, passWord))
                    .collect {
                        loginLiveDataPrivate.value = it
                    }
            }
        }
    }

    fun showToastMessage(errorCode: Int) {
        val error = errorManager.getError(errorCode)
        showToastPrivate.value = SingleEvent(error.description)
    }

    fun onTextFieldFocusChanged(key: FocusedTextFieldKey, isFocused: Boolean) {
        focusedTextField = if (isFocused) key else FocusedTextFieldKey.None
    }

    fun onEmailAddressEntered(input: String) {
        viewModelScope.launch {
            inputEvents.send(UserInputEvent.Email(input))
        }
    }

    fun onPasswordEntered(input: String) {
        viewModelScope.launch {
            inputEvents.send(UserInputEvent.Password(input))
        }
    }

    fun onLoginClick() {
        viewModelScope.launch {
            when (val inputErrors = getInputErrorsOrNull()) {
                null -> {
                    clearFocusAndHideKeyboard()
//                    doLogin("ahmed@ahmed.ahmed","ahmed")
                    doLogin(userName.value.value, password.value.value)
                }
                else -> displayInputErrors(inputErrors)
            }
        }
    }

    private fun getInputErrorsOrNull(): InputErrors? {
        val emailErrorId = InputValidator.getEmailErrorIdOrNull(userName.value.value)
        val passwordErrorId = InputValidator.getPasswordErrorIdOrNull(password.value.value)
        return if (emailErrorId == null) null else InputErrors(emailErrorId, passwordErrorId)
    }

    private suspend fun clearFocusAndHideKeyboard() {
        _events.send(ScreenEvent.ClearFocus)
        _events.send(ScreenEvent.UpdateKeyboard(false))
        focusedTextField = FocusedTextFieldKey.None
    }

    private fun focusOnLastSelectedTextField() {
        viewModelScope.launch {
            focusedTextField?.let { ScreenEvent.RequestFocus(it) }?.let { _events.send(it) }
            delay(250)
            _events.send(ScreenEvent.UpdateKeyboard(true))
        }
    }

    private fun observeUserInputEvents() {
        viewModelScope.launch {
            inputEvents.receiveAsFlow()
                .onEach { event ->
                    when (event) {
                        is UserInputEvent.Email -> {
                            when (InputValidator.getEmailErrorIdOrNull(event.input)) {
                                null -> {
                                    userName.value = userName.value.copy(
                                        value = event.input,
                                        errorId = null
                                    )
                                }
                                else -> userName.value =
                                    userName.value.copy(value = event.input)
                            }
                        }
                        is UserInputEvent.Password -> {
                            when (InputValidator.getPasswordErrorIdOrNull(event.input)) {
                                null -> {
                                    password.value = password.value.copy(
                                        value = event.input,
                                        errorId = null
                                    )
                                }
                                else -> password.value =
                                    password.value.copy(value = event.input)
                            }
                        }
                    }
                }
                .debounce(350)
                .collect { event ->
                    when (event) {
                        is UserInputEvent.Email -> {
                            val errorId = InputValidator.getEmailErrorIdOrNull(event.input)
                            userName.value = userName.value.copy(errorId = errorId)
                        }
                        is UserInputEvent.Password -> {
                            val errorId = InputValidator.getPasswordErrorIdOrNull(event.input)
                            password.value = password.value.copy(errorId = errorId)

                        }
                    }
                }
        }
    }

    private fun displayInputErrors(inputErrors: InputErrors) {
        userName.value = userName.value.copy(errorId = inputErrors.emailErrorId)
        password.value = password.value.copy(errorId = inputErrors.passwordErrorId)
    }

    fun showHideProgressBar(alpha: Float) {
        progressBar.value = alpha
    }
}
