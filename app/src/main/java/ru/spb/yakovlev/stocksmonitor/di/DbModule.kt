package ru.spb.yakovlev.stocksmonitor.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.spb.yakovlev.stocksmonitor.data.local.AppDb
import ru.spb.yakovlev.stocksmonitor.data.local.daos.*
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object DbModule {

    @Provides
    @Singleton
    fun provideAppDb(
        @ApplicationContext context: Context
    ): AppDb =
        Room.databaseBuilder(
            context,
            AppDb::class.java,
            AppDb.DATABASE_NAME
        ).build()

    @Provides
    @Singleton
    fun providesPriceDao(
        db: AppDb
    ): PriceDao =
        db.priceDao()

    @Provides
    @Singleton
    fun providesCompanyDao(
        db: AppDb
    ): CompanyDao =
        db.companyDao()

    @Provides
    @Singleton
    fun providesNewsDao(
        db: AppDb
    ): NewsDao =
        db.newsDao()

    @Provides
    @Singleton
    fun providesIndexDao(
        db: AppDb
    ): IndexDao =
        db.indexDao()

    @Provides
    @Singleton
    fun providesFavoriteDao(
        db: AppDb
    ): FavoriteDao =
        db.favoriteDao()

    @Provides
    @Singleton
    fun providesPrefsDao(
        db: AppDb
    ): PrefsDao =
        db.prefsDao()
}