package ru.spb.yakovlev.stocksmonitor.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import ru.spb.yakovlev.stocksmonitor.model.Stock
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

interface StocksDataProvider {
    val stocks: StateFlow<List<Stock>>
    val favorites: StateFlow<List<Stock>>
    val popularQueries: List<String>
    val resentQueries: StateFlow<List<String>>
    fun updateResentQueries(newQuery: String)
    fun getSearchResults(query: String): List<Stock>
    fun updateFavorites(ticker: String, isFavorite: Boolean): Boolean
    fun getIsFavoriteState(ticker: String): Flow<Boolean>
    fun getStockByTicker(ticker: String): Flow<Stock>
}

@Singleton
class MokkData @Inject constructor() : StocksDataProvider {
    private val mokkStocksList: MutableList<Stock> = generateMokkStocks()
    private val _stocks = MutableStateFlow<List<Stock>>(mokkStocksList)
    override val stocks: StateFlow<List<Stock>> = _stocks


    private val favoriteStocks = mutableSetOf<String>().apply {
        add("YNDX")
        add("AMZN")
    }

    override val popularQueries = mokkStocksList.map { it.ticker }

    private val _resentQueriesQueue = LinkedList<String>()
    private val _resentQueries = MutableStateFlow(_resentQueriesQueue.toList())
    override val resentQueries: StateFlow<List<String>> = _resentQueries

    override fun updateResentQueries(newQuery: String) {
        _resentQueriesQueue.remove(newQuery)
        _resentQueriesQueue.addLast(newQuery)
        if (_resentQueriesQueue.size > 20) _resentQueriesQueue.pollFirst()
        _resentQueries.value = _resentQueriesQueue.reversed()
    }

    private val _favorites =
        MutableStateFlow<List<Stock>>(mokkStocksList.filter { it.ticker in favoriteStocks })
    override val favorites: StateFlow<List<Stock>> = _favorites

    override fun getSearchResults(query: String): List<Stock> = mokkStocksList.filter {
        it.ticker.contains(query, true) || it.compName.contains(query, true)
    }

    override fun updateFavorites(ticker: String, isFavorite: Boolean): Boolean {
        if (isFavorite) favoriteStocks.add(ticker)
        else favoriteStocks.remove(ticker)
        _favorites.value = mokkStocksList.filter { it.ticker in favoriteStocks }
        return isFavorite
    }

    override fun getIsFavoriteState(ticker: String): Flow<Boolean> =
        favorites.map { list ->
            list.map { it.ticker }.contains(ticker)
        }

    override fun getStockByTicker(ticker: String): Flow<Stock> =
        stocks.map { list -> list.first() { it.ticker == ticker } }


    private fun generateMokkStocks(): MutableList<Stock> = mutableListOf(
        Stock(
            ticker = "YNDX",
            compName = "Yandex, LLC",
            price = 72.16f,
            currency = "$",
            delta = 0.12f,
            logo = "https://s3-alpha-sig.figma.com/img/5936/40ec/b752ca5f1fd9154cc5202d99bfdaf3c6?Expires=1617580800&Signature=Lf9PI4nh5dvhgT1LgGyFiGpmMgr6nM2LkBPt5PfZIasZN0heIlNRGcTK6Ehk7jJc3hJfFyDGmu872tT3e6MGWyM61ZcpowWBmQRRQlP0ONvCZYUQSWGJCklSDtxNpwiv3kowhjX-AOzoqleP-c6IVndOLCZ1lg7YTNcpbwjNzlET7xsYlF1uJa2bmzNqWhqE-LFH978iJBV5gRxk60Uib6HiXbBxuqOtkfeiKb5jj0ZW1h~VKsFDxjUBMDESHAGta71WP0~pbe4g79S~ZuEX74BshXZALlX0EBmEL99m~fSWgI-2ZedGxuD23Nc1SvmzPthKAcDQ5r~QWjqbx-RbVg__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA"
        ),
        Stock(
            ticker = "AAPL",
            compName = "Apple Inc.",
            price = 131.93f,
            currency = "$",
            delta = -5.11f,
            logo = "https://s3-alpha-sig.figma.com/img/d46d/6c6d/4328efc1c87c40cf5bf577b33885eead?Expires=1617580800&Signature=D08Pd-KbjOLbRMQLrQ9QFYPvPav4271PTu~btUrkU6s7WvZRW-zpdUwrDzVGrCI9l-Yptom5gQjEnjNlEfWKXkmlmDn-pTIz0HwW7zW3wR0qQFlygRbcChs7GIU4w-ndQ0U~LzrN8SZwWbc52wuPYxqqJ~x09QkSdPbGjwsY4T91yy3jGvvc6PIyP3-FLh3zC3LUGlBcSlkNjdAHRhINrAnErOE-ePblDufuwNMLCKBBxOhYWcznqI1eJSh2Qo35u5TV0w1SRxmUDTv11izFND5sD56ts2yj40lddCKvWspf27WsOx3RlpxB1FZkAxO2OG4lhmo6bzU2mFOnqQgWtw__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA"
        ),
        Stock(
            ticker = "GOOGL",
            compName = "Alphabet Class A",
            price = 1825f,
            currency = "$",
            delta = 25.12f,
            logo = "https://s3-alpha-sig.figma.com/img/f7db/adaf/17ffff97084ff06d69d9f7a7f9a12a06?Expires=1617580800&Signature=SATdne~z3aqcHzkRiGbvb3oG5gICxzenTJQ8UJw-LrLXc6PA0BTlU~JXQcqJrK~r7~QnfZXA9SAI7B9YllA1N1HJ0zLb3~XS~SnYHb11Pxw9yFJzYxXCnYPZSSu97pvJB-o7o5V8kh8CV4BP-sWixS~VIbi~9Za7YdpR4IIVDLefeLECLQvax9WnY4YF4gI4WysKua2dZA7miOw-L7-lTiqo4II71Ttmgqe~6ueYyktLBFXqO1SJdwTcG16KcdtCH5n0UJFfriAG9Tzdq14cO5YU37vKCmkpzonVwP-prf~y~wAsQwjSDEuDrfJ-2nA0ARs83lITfl77OWDJdVMjwA__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA"
        ),
        Stock(
            ticker = "AMZN",
            compName = "Amazon.com",
            price = 3204f,
            currency = "$",
            delta = 31.12f,
            logo = "https://s3-alpha-sig.figma.com/img/907c/c0d1/15d5fb79422a0f0327f092ddef7e2c07?Expires=1617580800&Signature=Yx9hShhDx1A0IPW6YaIJfmdp5pv6OErotB7T~DmaIvx5ha5eXeFP6LzUg97ABQG4rXcQ4kTE65J45Y7JVreY2~f5kOWeBpPLht3LP-AUdT1PAaYqU2FZ6zjq3aK6ZEg9cAkfU0UQZ4IwhmsRVgfcf9lmhnwALC9bzo9ClaOuCmoEgR886zN9V69sTqCCkiJhgv5sLvhaIwb-rkj~PucDen4Ouk~wVlXPqvjAirBCQaTyGCSugfdPDZEamoQOaFmFX9UIGibfy7qpaJfB22wnPEc6Cu0nyAIL7c3e3dFqVCSoNYG1eYY2zoNfcbhZQPvYfbdok8kG2-AziPIrRD2DZg__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA"
        ),
        Stock(
            ticker = "YNDX1",
            compName = "Yandex, LLC",
            price = 72.16f,
            currency = "$",
            delta = 0.12f,
            logo = "https://s3-alpha-sig.figma.com/img/5936/40ec/b752ca5f1fd9154cc5202d99bfdaf3c6?Expires=1617580800&Signature=Lf9PI4nh5dvhgT1LgGyFiGpmMgr6nM2LkBPt5PfZIasZN0heIlNRGcTK6Ehk7jJc3hJfFyDGmu872tT3e6MGWyM61ZcpowWBmQRRQlP0ONvCZYUQSWGJCklSDtxNpwiv3kowhjX-AOzoqleP-c6IVndOLCZ1lg7YTNcpbwjNzlET7xsYlF1uJa2bmzNqWhqE-LFH978iJBV5gRxk60Uib6HiXbBxuqOtkfeiKb5jj0ZW1h~VKsFDxjUBMDESHAGta71WP0~pbe4g79S~ZuEX74BshXZALlX0EBmEL99m~fSWgI-2ZedGxuD23Nc1SvmzPthKAcDQ5r~QWjqbx-RbVg__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA"
        ),
        Stock(
            ticker = "AAPL1",
            compName = "Apple Inc.",
            price = 131.93f,
            currency = "$",
            delta = -5.11f,
            logo = "https://s3-alpha-sig.figma.com/img/d46d/6c6d/4328efc1c87c40cf5bf577b33885eead?Expires=1617580800&Signature=D08Pd-KbjOLbRMQLrQ9QFYPvPav4271PTu~btUrkU6s7WvZRW-zpdUwrDzVGrCI9l-Yptom5gQjEnjNlEfWKXkmlmDn-pTIz0HwW7zW3wR0qQFlygRbcChs7GIU4w-ndQ0U~LzrN8SZwWbc52wuPYxqqJ~x09QkSdPbGjwsY4T91yy3jGvvc6PIyP3-FLh3zC3LUGlBcSlkNjdAHRhINrAnErOE-ePblDufuwNMLCKBBxOhYWcznqI1eJSh2Qo35u5TV0w1SRxmUDTv11izFND5sD56ts2yj40lddCKvWspf27WsOx3RlpxB1FZkAxO2OG4lhmo6bzU2mFOnqQgWtw__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA"
        ),
        Stock(
            ticker = "GOOGL1",
            compName = "Alphabet Class A",
            price = 1825f,
            currency = "$",
            delta = 25.12f,
            logo = "https://s3-alpha-sig.figma.com/img/f7db/adaf/17ffff97084ff06d69d9f7a7f9a12a06?Expires=1617580800&Signature=SATdne~z3aqcHzkRiGbvb3oG5gICxzenTJQ8UJw-LrLXc6PA0BTlU~JXQcqJrK~r7~QnfZXA9SAI7B9YllA1N1HJ0zLb3~XS~SnYHb11Pxw9yFJzYxXCnYPZSSu97pvJB-o7o5V8kh8CV4BP-sWixS~VIbi~9Za7YdpR4IIVDLefeLECLQvax9WnY4YF4gI4WysKua2dZA7miOw-L7-lTiqo4II71Ttmgqe~6ueYyktLBFXqO1SJdwTcG16KcdtCH5n0UJFfriAG9Tzdq14cO5YU37vKCmkpzonVwP-prf~y~wAsQwjSDEuDrfJ-2nA0ARs83lITfl77OWDJdVMjwA__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA"
        ),
        Stock(
            ticker = "AMZN1",
            compName = "Amazon.com",
            price = 3204f,
            currency = "$",
            delta = 31.12f,
            logo = "https://s3-alpha-sig.figma.com/img/907c/c0d1/15d5fb79422a0f0327f092ddef7e2c07?Expires=1617580800&Signature=Yx9hShhDx1A0IPW6YaIJfmdp5pv6OErotB7T~DmaIvx5ha5eXeFP6LzUg97ABQG4rXcQ4kTE65J45Y7JVreY2~f5kOWeBpPLht3LP-AUdT1PAaYqU2FZ6zjq3aK6ZEg9cAkfU0UQZ4IwhmsRVgfcf9lmhnwALC9bzo9ClaOuCmoEgR886zN9V69sTqCCkiJhgv5sLvhaIwb-rkj~PucDen4Ouk~wVlXPqvjAirBCQaTyGCSugfdPDZEamoQOaFmFX9UIGibfy7qpaJfB22wnPEc6Cu0nyAIL7c3e3dFqVCSoNYG1eYY2zoNfcbhZQPvYfbdok8kG2-AziPIrRD2DZg__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA"
        ),
        Stock(
            ticker = "YNDX2",
            compName = "Yandex, LLC",
            price = 72.16f,
            currency = "$",
            delta = 0.12f,
            logo = "https://s3-alpha-sig.figma.com/img/5936/40ec/b752ca5f1fd9154cc5202d99bfdaf3c6?Expires=1617580800&Signature=Lf9PI4nh5dvhgT1LgGyFiGpmMgr6nM2LkBPt5PfZIasZN0heIlNRGcTK6Ehk7jJc3hJfFyDGmu872tT3e6MGWyM61ZcpowWBmQRRQlP0ONvCZYUQSWGJCklSDtxNpwiv3kowhjX-AOzoqleP-c6IVndOLCZ1lg7YTNcpbwjNzlET7xsYlF1uJa2bmzNqWhqE-LFH978iJBV5gRxk60Uib6HiXbBxuqOtkfeiKb5jj0ZW1h~VKsFDxjUBMDESHAGta71WP0~pbe4g79S~ZuEX74BshXZALlX0EBmEL99m~fSWgI-2ZedGxuD23Nc1SvmzPthKAcDQ5r~QWjqbx-RbVg__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA"
        ),
        Stock(
            ticker = "AAPL2",
            compName = "Apple Inc.",
            price = 131.93f,
            currency = "$",
            delta = -5.11f,
            logo = "https://s3-alpha-sig.figma.com/img/d46d/6c6d/4328efc1c87c40cf5bf577b33885eead?Expires=1617580800&Signature=D08Pd-KbjOLbRMQLrQ9QFYPvPav4271PTu~btUrkU6s7WvZRW-zpdUwrDzVGrCI9l-Yptom5gQjEnjNlEfWKXkmlmDn-pTIz0HwW7zW3wR0qQFlygRbcChs7GIU4w-ndQ0U~LzrN8SZwWbc52wuPYxqqJ~x09QkSdPbGjwsY4T91yy3jGvvc6PIyP3-FLh3zC3LUGlBcSlkNjdAHRhINrAnErOE-ePblDufuwNMLCKBBxOhYWcznqI1eJSh2Qo35u5TV0w1SRxmUDTv11izFND5sD56ts2yj40lddCKvWspf27WsOx3RlpxB1FZkAxO2OG4lhmo6bzU2mFOnqQgWtw__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA"
        ),
        Stock(
            ticker = "GOOGL2",
            compName = "Alphabet Class A",
            price = 1825f,
            currency = "$",
            delta = 25.12f,
            logo = "https://s3-alpha-sig.figma.com/img/f7db/adaf/17ffff97084ff06d69d9f7a7f9a12a06?Expires=1617580800&Signature=SATdne~z3aqcHzkRiGbvb3oG5gICxzenTJQ8UJw-LrLXc6PA0BTlU~JXQcqJrK~r7~QnfZXA9SAI7B9YllA1N1HJ0zLb3~XS~SnYHb11Pxw9yFJzYxXCnYPZSSu97pvJB-o7o5V8kh8CV4BP-sWixS~VIbi~9Za7YdpR4IIVDLefeLECLQvax9WnY4YF4gI4WysKua2dZA7miOw-L7-lTiqo4II71Ttmgqe~6ueYyktLBFXqO1SJdwTcG16KcdtCH5n0UJFfriAG9Tzdq14cO5YU37vKCmkpzonVwP-prf~y~wAsQwjSDEuDrfJ-2nA0ARs83lITfl77OWDJdVMjwA__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA"
        ),
        Stock(
            ticker = "AMZN2",
            compName = "Amazon.com",
            price = 3204f,
            currency = "$",
            delta = 31.12f,
            logo = "https://s3-alpha-sig.figma.com/img/907c/c0d1/15d5fb79422a0f0327f092ddef7e2c07?Expires=1617580800&Signature=Yx9hShhDx1A0IPW6YaIJfmdp5pv6OErotB7T~DmaIvx5ha5eXeFP6LzUg97ABQG4rXcQ4kTE65J45Y7JVreY2~f5kOWeBpPLht3LP-AUdT1PAaYqU2FZ6zjq3aK6ZEg9cAkfU0UQZ4IwhmsRVgfcf9lmhnwALC9bzo9ClaOuCmoEgR886zN9V69sTqCCkiJhgv5sLvhaIwb-rkj~PucDen4Ouk~wVlXPqvjAirBCQaTyGCSugfdPDZEamoQOaFmFX9UIGibfy7qpaJfB22wnPEc6Cu0nyAIL7c3e3dFqVCSoNYG1eYY2zoNfcbhZQPvYfbdok8kG2-AziPIrRD2DZg__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA"
        ),
    )


}