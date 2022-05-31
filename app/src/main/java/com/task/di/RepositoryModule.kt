package com.task.di

import com.task.data.local.UserLocalDataSource
import com.task.data.local.recipe.RecipeLocalDataSource
import com.task.data.remote.recipe.RecipeRemoteData
import com.task.data.repository.login.UserRepository
import com.task.data.repository.login.UserRepositoryImpl
import com.task.data.repository.recipe.RecipeRepository
import com.task.data.repository.recipe.RecipeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideLoginRepository(
        userLocalDataSource: UserLocalDataSource,
        ioDispatcher: CoroutineContext,
    ): UserRepository = UserRepositoryImpl(userLocalDataSource, ioDispatcher)

    @Provides
    @ViewModelScoped
    fun provideRecipeRepository(
        recipeRemoteData: RecipeRemoteData,
        recipeLocalDataSource: RecipeLocalDataSource,
    ): RecipeRepository = RecipeRepositoryImpl(recipeRemoteData, recipeLocalDataSource)

}