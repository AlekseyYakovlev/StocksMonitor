package ru.spb.yakovlev.stocksmonitor.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class NetworkMonitor (
    val context: Context
) {
    var isConnected: Boolean = true
    val isConnectedLive = MutableLiveData(true)
    val networkTypeLive = MutableLiveData(NetworkType.NONE)

    private lateinit var cm: ConnectivityManager

    init {
        registerNetworkMonitor()
    }

    private fun registerNetworkMonitor() {
        cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        obtainNetworkType(cm.activeNetwork?.let { cm.getNetworkCapabilities(it) })
            .also { networkTypeLive.postValue(it) }

        cm.registerNetworkCallback(
            NetworkRequest.Builder().build(),
            object : ConnectivityManager.NetworkCallback() {
                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    networkTypeLive.postValue(obtainNetworkType(networkCapabilities))
                }

                override fun onLost(network: Network) {
                    isConnected = false
                    isConnectedLive.postValue(false)
                    networkTypeLive.postValue(NetworkType.NONE)
                }

                override fun onAvailable(network: Network) {
                    isConnected = true
                    isConnectedLive.postValue(true)
                }
            }
        )
    }

    private fun obtainNetworkType(networkCapabilities: NetworkCapabilities?): NetworkType = when {
        networkCapabilities == null -> NetworkType.NONE
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkType.ETHERNET
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> NetworkType.BLUETOOTH
        else -> NetworkType.UNKNOWN
    }
}

enum class NetworkType {
    NONE, UNKNOWN, WIFI, CELLULAR, ETHERNET, BLUETOOTH
}