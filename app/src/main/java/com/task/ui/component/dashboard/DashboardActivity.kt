package com.task.ui.component.dashboard

import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.task.R
import com.task.databinding.DashboardActivityBinding
import com.task.ui.base.BaseActivity
import com.task.utils.SingleEvent
import com.task.utils.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DashboardActivity : BaseActivity() {

    private val dasboardViewModel: DashBoardViewModel by viewModels()
    private lateinit var binding: DashboardActivityBinding

    fun observeToast(event: LiveData<SingleEvent<Any>>) {
        binding.root.showToast(this, event, Snackbar.LENGTH_LONG)
    }

    override fun observeViewModel() {
        observeToast(dasboardViewModel.showToast)
    }

    override fun initViewBinding() {
        binding = DashboardActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)
        setupAppNaviagtion()
    }

    private fun setupAppNaviagtion() {
        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.homeFragment
        )
            .build()
        val navController: NavController = Navigation.findNavController(this, R.id.nav_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)
    }
}