package com.task.ui.trade

import com.task.data.DataRepositorySource
import com.task.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TradeFragmentViewModel @Inject constructor(private val dataRepositoryRepository: DataRepositorySource)  : BaseViewModel(){
}