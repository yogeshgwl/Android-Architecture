package com.task.ui.school

import com.task.data.DataRepositorySource
import com.task.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SchoolFragmentViewModel  @Inject constructor(private val dataRepositoryRepository: DataRepositorySource) : BaseViewModel(){
}