package ru.spb.yakovlev.stocksmonitor.ui.main

import ru.spb.yakovlev.stocksmonitor.model.Stock
import ru.spb.yakovlev.stocksmonitor.ui.base.RvItemData

data class StockItemData(
    override val id: String,
    val ticker: String = "",
    val compName: String = "",
    val price: Float = 0f,
    val currency: String = "",
    val delta: Float = 0f,
    val logo: String = "",
) : RvItemData

fun Stock.toStockItemData(isFavorite: Boolean) =
    StockItemData(
        id = ticker,
        ticker = ticker,
        compName = compName,
        price = price,
        currency = currency,
        delta = delta,
        logo = logo,
    )