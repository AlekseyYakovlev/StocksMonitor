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
import ru.spb.yakovlev.stocksmonitor.data.remote.FinHubRestService
import ru.spb.yakovlev.stocksmonitor.data.remote.adapters.DateAdapter
import ru.spb.yakovlev.stocksmonitor.data.remote.interceptors.ApiKeyAuthenticator
import ru.spb.yakovlev.stocksmonitor.data.remote.interceptors.ErrorStatusInterceptor
import ru.spb.yakovlev.stocksmonitor.data.remote.interceptors.NetworkStatusInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object NetworkModule {

    @Provides
    @Singleton
    fun providesRestService(
        retrofit: Retrofit
    ): FinHubRestService =
        retrofit.create(FinHubRestService::class.java)

    @Provides
    @Singleton
    fun providesRetrofit(
        client: OkHttpClient,
        moshi: Moshi
    ): Retrofit =
        Retrofit.Builder()
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .baseUrl(BuildConfig.FIN_HUB_BASE_URL)
            .build()

    @Provides
    @Singleton
    fun providesOkHttpClient(
        apiKeyAuthenticator: ApiKeyAuthenticator,
        networkStatusInterceptor: NetworkStatusInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        errorStatusInterceptor: ErrorStatusInterceptor
    ): OkHttpClient =
        OkHttpClient().newBuilder()
            .readTimeout(25, TimeUnit.SECONDS)  // socket timeout (GET)
            .writeTimeout(5, TimeUnit.SECONDS) // socket timeout (POST, PUT, etc.)
            .addInterceptor(networkStatusInterceptor)  // intercept network status
            .addInterceptor(apiKeyAuthenticator)       // add token for every request
            .addInterceptor(httpLoggingInterceptor)    // log requests/results
            .addInterceptor(errorStatusInterceptor)    // intercept network errors
            .build()

    @Provides
    @Singleton
    fun provideApiKeyAuthenticator(): ApiKeyAuthenticator =
        ApiKeyAuthenticator(BuildConfig.FIN_HUB_API_KEY)

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

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
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(DateAdapter())
            .add(KotlinJsonAdapterFactory()) // for reflection
            .build()
}