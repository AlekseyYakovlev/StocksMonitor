package ru.spb.yakovlev.stocksmonitor.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.spb.yakovlev.stocksmonitor.BuildConfig
import ru.spb.yakovlev.stocksmonitor.data.remote.NetworkMonitor
import ru.spb.yakovlev.stocksmonitor.data.remote.RestService
import ru.spb.yakovlev.stocksmonitor.data.remote.adapters.DateAdapter
import ru.spb.yakovlev.stocksmonitor.data.remote.interceptors.ErrorStatusInterceptor
import ru.spb.yakovlev.stocksmonitor.data.remote.interceptors.NetworkStatusInterceptor
import ru.spb.yakovlev.stocksmonitor.data.remote.interceptors.TokenAuthenticator
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun providesRestService(
        retrofit: Retrofit
    ): RestService =
        retrofit.create(RestService::class.java)

    @Provides
    @Singleton
    fun providesRetrofit(
        client: OkHttpClient,
        moshi: Moshi
    ): Retrofit =
        Retrofit.Builder()
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BuildConfig.BASE_URL)
            .build()

    @Provides
    @Singleton
    fun providesOkHttpClient(
        tokenAuthenticator: TokenAuthenticator,
        networkStatusInterceptor: NetworkStatusInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        errorStatusInterceptor: ErrorStatusInterceptor
    ): OkHttpClient =
        OkHttpClient().newBuilder()
            .readTimeout(2, TimeUnit.SECONDS)  // socket timeout (GET)
            .writeTimeout(5, TimeUnit.SECONDS) // socket timeout (POST, PUT, etc.)
            .authenticator(tokenAuthenticator)         // refresh token if response code == 401
            .addInterceptor(networkStatusInterceptor)  // intercept network status
            .addInterceptor(httpLoggingInterceptor)    // log requests/results
            .addInterceptor(errorStatusInterceptor)    // intercept network errors
            .build()

    @Provides
    @Singleton
    fun provideNetworkStatusInterceptor(
        monitor: NetworkMonitor
    ): NetworkStatusInterceptor =
        NetworkStatusInterceptor(monitor)

    @Provides
    @Singleton
    fun provideErrorStatusInterceptor(
        moshi: Moshi
    ): ErrorStatusInterceptor =
        ErrorStatusInterceptor(moshi)

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
    ): TokenAuthenticator =
        TokenAuthenticator()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(DateAdapter())
            .add(KotlinJsonAdapterFactory()) // for reflection
            .build()
}