package com.task.ui.explore

import com.task.data.DataRepositorySource
import com.task.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExploreFragmentViewModel @Inject constructor(private val dataRepository: DataRepositorySource)  :BaseViewModel() {
}