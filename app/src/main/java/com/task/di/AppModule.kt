package com.task.di

import android.content.Context
import com.task.data.datastore.manager.FavouriteProtoDataStoreManager
import com.task.data.local.UserLocalDataSource
import com.task.utils.Network
import com.task.utils.NetworkConnectivity
import com.task.utils.analytics.AppAnalyticsImpl
import com.task.utils.logs.AppLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideFavouriteProtoDataStoreManager(@ApplicationContext context: Context): FavouriteProtoDataStoreManager =
        FavouriteProtoDataStoreManager(context)

    @Provides
    @Singleton
    fun provideLoginLocalDataSource(@ApplicationContext context: Context): UserLocalDataSource =
        UserLocalDataSource(context)

    @Provides
    @Singleton
    fun provideCoroutineContext(): CoroutineContext {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivity(@ApplicationContext context: Context): NetworkConnectivity {
        return Network(context)
    }

    @Provides
    @Singleton
    fun provideAppAnalytics(@ApplicationContext context: Context): AppAnalyticsImpl {
        return AppAnalyticsImpl(context)
    }

    @Provides
    @Singleton
    fun provideApplicationLogger(): AppLogger {
        return AppLogger
    }
}
