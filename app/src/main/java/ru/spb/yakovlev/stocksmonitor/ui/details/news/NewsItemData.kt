package ru.spb.yakovlev.stocksmonitor.ui.details.news

import ru.spb.yakovlev.stocksmonitor.model.News
import ru.spb.yakovlev.stocksmonitor.ui.base.RvItemData
import java.util.*

data class NewsItemData(
    override val id: String,
    val category: String = "",
    val datetime: Date = Date(),
    val headline: String = "",
    val image: String = "",
    val related: String = "", //ticker
    val source: String = "",
    val summary: String = "",
    val url: String = "",
) : RvItemData

fun News.toNewsItemData() =
    NewsItemData(
        id = id,
        category = category,
        datetime = datetime,
        headline = headline,
        image = image,
        related = related,
        source = "$source ($url)",
        summary = summary,
    )