package ru.spb.yakovlev.stocksmonitor.ui.main

import ru.spb.yakovlev.stocksmonitor.model.Company
import ru.spb.yakovlev.stocksmonitor.model.Stock
import ru.spb.yakovlev.stocksmonitor.ui.base.RvItemData

data class StockItemData(
    override val id: String,
    val ticker: String = "",
    val compName: String = "",
    val logo: String = "",
) : RvItemData

fun Company.toStockItemData() =
    StockItemData(
        id = ticker,
        ticker = ticker,
        compName = name,
        logo = logo,
    )