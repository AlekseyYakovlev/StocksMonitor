package ru.spb.yakovlev.stocksmonitor.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ru.spb.yakovlev.stocksmonitor.data.remote.responses.*
import java.text.SimpleDateFormat
import java.util.*

interface FinHubRestService {


   //https://finnhub.io/api/v1/index/constituents?symbol=^GSPC&token=<apiKey>
   @GET("index/constituents")
    suspend fun loadIndices(
       @Query("symbol") indexName: String = "^DJI"
    ):IndexResponse

   // https://finnhub.io/api/v1/stock/profile2?symbol=AAPL&token=<apiKey>
   @GET("stock/profile2")
   suspend fun loadCompanyProfile(
       @Query("symbol") ticker: String = "AAPL"
   ): CompanyProfileResponse

   // https://finnhub.io/api/v1/company-news?symbol=AAPL&from=2021-03-01&to=2021-03-09&token=<apiKey>
   @GET("company-news")
   suspend fun loadCompanyNews(
       @Query("symbol") ticker: String = "AAPL",
       @Query("from") from: String = "2021-01-01",
       @Query("to") to: String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()),
   ): List<CompanyNewsResponse>

    //https://finnhub.io/api/v1/stock/candle?symbol=IBM&resolution=D&from=1572651390&to=<apiKey>
    @GET("stock/candle")
    suspend fun loadCandles(
        @Query("symbol") ticker: String = "AAPL",
        @Query("resolution") resolution: String = "D",
        @Query("from") from: Long = Date().time/1000,
        @Query("to") to: Long = Date().time/1000,
    ): StockCandlesResponse

    //https://finnhub.io/api/v1/search?q=apple&token=<apiKey>
    @GET("search")
    suspend fun search(
        @Query("q") query: String = "",
    ): SearchResponse

    //https://finnhub.io/api/v1/quote?symbol=AAPL&token=<apiKey>
    @GET("quote")
    suspend fun loadDayPrice(
        @Query("symbol") symbol: String = "",
    ): QuotesResponse


}

