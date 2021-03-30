package ru.spb.yakovlev.stocksmonitor.data.remote.interceptors

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.spb.yakovlev.stocksmonitor.BuildConfig
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(

) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        return if (response.code == 401) {
            getNewToken().let {
                response.request.newBuilder()
                    .header("Authorization", it)
                    .build()
            }
        } else null
    }

    private fun getNewToken(): String = BuildConfig.API_KEY

}