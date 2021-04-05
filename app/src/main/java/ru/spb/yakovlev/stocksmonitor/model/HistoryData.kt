package ru.spb.yakovlev.stocksmonitor.model

data class HistoryData(
    val ticker: String = "",
    val dates: List<Long> = emptyList(),
    val prices: List<Double> = emptyList(),
    val status: Status = Status.OK
) {
    enum class Status {
        OK,
        ERROR
    }
}