package com.task.ui.component.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar
import com.task.data.Resource
import com.task.data.dto.login.LoginResponse
import com.task.databinding.LoginActivityBinding
import com.task.ui.base.BaseActivity
import com.task.ui.component.dashboard.DashboardActivity
import com.task.utils.*
import com.task.utils.analytics.AppAnalyticsImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var binding: LoginActivityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.login.setOnClickListener { doLogin() }
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
