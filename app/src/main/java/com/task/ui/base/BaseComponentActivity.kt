package com.task.ui.base

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.task.utils.analytics.AppAnalyticsImpl
import com.task.utils.logs.AppLogger
import javax.inject.Inject


abstract class BaseComponentActivity : AppCompatActivity() {

    @Inject lateinit var appLogger: AppLogger
    @Inject lateinit var appAnalyticsImpl: AppAnalyticsImpl

    abstract fun observeViewModel()
    protected abstract fun initViewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
        observeViewModel()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}
