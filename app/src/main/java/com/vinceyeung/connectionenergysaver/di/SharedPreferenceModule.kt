package com.vinceyeung.connectionenergysaver.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.vinceyeung.connectionenergysaver.utilities.PREFERENCE_FILE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object SharedPreferenceModule {

    @Singleton
    @Provides
    fun provideSharedPreference(app: Application): SharedPreferences {
        return app.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)
    }
}