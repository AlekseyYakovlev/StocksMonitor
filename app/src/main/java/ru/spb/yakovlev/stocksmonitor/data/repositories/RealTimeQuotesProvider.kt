package ru.spb.yakovlev.stocksmonitor.data.repositories

import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import ru.spb.yakovlev.stocksmonitor.BuildConfig
import ru.spb.yakovlev.stocksmonitor.data.remote.responses.TradesResponse
import ru.spb.yakovlev.stocksmonitor.model.DataState
import timber.log.Timber
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.SSLSocketFactory


@Singleton
class RealTimeQuotesProvider @Inject constructor(
    private val moshi: Moshi
) {
    private val _prices = MutableStateFlow<DataState<List<TradesResponse.Data>>>(DataState.Empty)
    val prices: StateFlow<DataState<List<TradesResponse.Data>>> = _prices


    private lateinit var webSocketClient: WebSocketClient

    private val subscriptionsList = mutableListOf<String>()

    fun updateSubscriptionsList(newList: List<String>) {
        val addedElements = newList.filter { it !in subscriptionsList }
        addedElements.forEach { subscribe(it) }

        val removedElements = subscriptionsList.filter { it !in newList }
        removedElements.forEach { unsubscribe(it) }

        subscriptionsList.clear()
        subscriptionsList.addAll(newList)
    }

    fun initWebSocket() {
        val connectionUri: URI = URI("${BuildConfig.WEB_SOCKET}?token=${BuildConfig.FIN_HUB_API_KEY}")

        createWebSocketClient(connectionUri)

        val socketFactory: SSLSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory
        webSocketClient.setSocketFactory(socketFactory)

        webSocketClient.connect()
    }

    fun closeWebSocket() {
        webSocketClient.close()
    }

    private fun createWebSocketClient(coinbaseUri: URI) {
        webSocketClient = object : WebSocketClient(coinbaseUri) {

            override fun onOpen(handshakedata: ServerHandshake?) {
                _prices.value = DataState.Loading
                updateSubscriptionsList(subscriptionsList)
            }

            override fun onMessage(message: String?) {
                message?.let { msg ->
                    Timber.tag("123456").d(msg)
                    moshi.adapter(TradesResponse::class.java)
                        .fromJson(msg)
                        ?.data
                        ?.takeIf { it.isNotEmpty() }
                        ?.let { _prices.value = DataState.Success(it) }
                }
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                subscriptionsList.forEach { unsubscribe(it) }
            }

            override fun onError(ex: Exception?) {
                _prices.value = DataState.Error(ex)
            }
        }
    }

    private fun subscribe(symbol: String) {
        webSocketClient.send("{\"type\":\"subscribe\",\"symbol\":\"$symbol\"}")
    }

    private fun unsubscribe(symbol: String) {
        webSocketClient.send("{\"type\":\"unsubscribe\",\"symbol\":\"$symbol\"}")
    }

}