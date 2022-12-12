package com.jiemaibj.metro.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jiemaibj.metro.data.LoginDataSource
import com.jiemaibj.metro.utilities.SM2Util
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    fun provideLoginDataSource(): LoginDataSource {
        return LoginDataSource()
    }

    @Provides
    fun provideSM2Util(): SM2Util {
        return SM2Util()
    }

    @ApplicationScope
    @Singleton
    @Provides
    fun providesApplicationScope() = CoroutineScope(SupervisorJob() + Dispatchers.Default)
}