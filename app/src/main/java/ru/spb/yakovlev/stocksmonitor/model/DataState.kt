package ru.spb.yakovlev.stocksmonitor.model

sealed class DataState<out T : Any> {
    object Empty:DataState<Nothing>()
    object Loading:DataState<Nothing>()
    data class Error(val throwable: Throwable?):DataState<Nothing>()
    data class Success<out T : Any>(val data: T):DataState<T>()
}