package ru.spb.yakovlev.stocksmonitor.di

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
internal object ActivityModule {
    @Provides
    fun providesActivity(
        activity: Activity
    ): AppCompatActivity = activity as AppCompatActivity
}