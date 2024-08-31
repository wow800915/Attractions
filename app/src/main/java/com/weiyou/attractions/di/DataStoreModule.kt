package com.weiyou.attractions.di

import android.content.Context
import com.weiyou.attractions.utils.LanguageDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideLanguageDataStore(@ApplicationContext context: Context): LanguageDataStore {
        return LanguageDataStore(context)
    }
}