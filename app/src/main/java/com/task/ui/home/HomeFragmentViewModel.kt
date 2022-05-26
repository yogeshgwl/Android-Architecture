package com.task.ui.home

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.task.data.Resource
import com.task.data.dto.recipes.Recipes
import com.task.data.dto.recipes.RecipesItem
import com.task.data.repository.recipe.RecipeRepositoryImpl
import com.task.ui.base.BaseViewModel
import com.task.utils.SingleEvent
import com.task.utils.wrapEspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(private val recipeRepositoryImplSource: RecipeRepositoryImpl) :
    BaseViewModel() {

    /**
     * Data --> LiveData, Exposed as LiveData, Locally in viewModel as MutableLiveData
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val recipesLiveDataPrivate = MutableLiveData<Resource<Recipes>>()
    val recipesLiveData: LiveData<Resource<Recipes>> get() = recipesLiveDataPrivate

    /**
     * UI actions as event, user action is single one time event, Shouldn't be multiple time consumption
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val openRecipeDetailsPrivate = MutableLiveData<SingleEvent<RecipesItem>>()
    val openRecipeDetails: LiveData<SingleEvent<RecipesItem>> get() = openRecipeDetailsPrivate


    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast: LiveData<SingleEvent<Any>> get() = showToastPrivate


    fun openRecipeDetails(recipe: RecipesItem) {
        openRecipeDetailsPrivate.value = SingleEvent(recipe)
    }

    fun showToastMessage(errorCode: Int) {
        val error = errorManager.getError(errorCode)
        showToastPrivate.value = SingleEvent(error.description)
    }

    fun getRecipes() {
        viewModelScope.launch {
            recipesLiveDataPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                recipeRepositoryImplSource.requestRecipes().collect {
                    recipesLiveDataPrivate.value = it
                }
            }
        }
    }


}