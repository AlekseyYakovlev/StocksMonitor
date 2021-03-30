package ru.spb.yakovlev.stocksmonitor.data.remote.errors

import java.io.IOException

class NoNetworkError(override val message: String = "Network is not available") : IOException(message)