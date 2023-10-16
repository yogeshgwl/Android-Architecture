package com.task.di

import com.task.TestRecipeRepository
import com.task.data.repository.recipe.RecipeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class TestDataModule {
    @Binds
    @Singleton
    abstract fun provideRecipeRepository(recipeRepository: TestRecipeRepository): RecipeRepository
}
