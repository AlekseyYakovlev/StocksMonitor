package ru.spb.yakovlev.stocksmonitor.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import ru.spb.yakovlev.stocksmonitor.data.remote.NetworkMonitor
import ru.spb.yakovlev.stocksmonitor.data.remote.errors.NoNetworkError

class NetworkStatusInterceptor(private val monitor: NetworkMonitor) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!monitor.isConnected) throw NoNetworkError()

        return chain.proceed(chain.request())
    }
}