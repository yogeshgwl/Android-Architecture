package com.task.ui.component.dashboard

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.task.R
import com.task.data.repository.recipe.RecipeRepositoryImpl
import com.task.ui.base.BaseViewModel
import com.task.utils.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class DashBoardViewModel @Inject constructor(private val RecipeRepositoryImpl: RecipeRepositoryImpl) :
    BaseViewModel() {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast: LiveData<SingleEvent<Any>> get() = showToastPrivate

    private val _tabState = MutableStateFlow(
        DashboardTabState(
            titles = listOf(R.string.bottom_tab_home, R.string.bottom_tab_search),
            currentIndex = 0
        )
    )
    val tabState: StateFlow<DashboardTabState> = _tabState.asStateFlow()

    fun showToastMessage(errorCode: Int) {
        val error = errorManager.getError(errorCode)
        showToastPrivate.value = SingleEvent(error.description)
    }

    fun switchTab(newIndex: Int) {
        if (newIndex != tabState.value.currentIndex) {
            _tabState.update {
                it.copy(currentIndex = newIndex)
            }
        }
    }
}

data class DashboardTabState(
    val titles: List<Int>,
    val currentIndex: Int
)

sealed interface DashboardUiState {
    object Loading : DashboardUiState

    data class Dashboard(
        val home: String,
        val search: String
    ) : DashboardUiState

    object Empty : DashboardUiState
}