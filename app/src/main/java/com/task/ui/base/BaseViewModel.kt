package com.task.ui.base

import androidx.lifecycle.ViewModel
import com.task.usecase.errors.ErrorManager
import com.task.utils.analytics.AppAnalyticsImpl
import com.task.utils.logs.AppLogger
import javax.inject.Inject


abstract class BaseViewModel : ViewModel() {
    /**Inject Singleton ErrorManager
     * Use this errorManager to get the Errors
     */
    @Inject
    lateinit var errorManager: ErrorManager
    @Inject
    lateinit var appLogger: AppLogger
    @Inject
    lateinit var appAnalyticsImpl: AppAnalyticsImpl
}
