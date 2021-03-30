package ru.spb.yakovlev.stocksmonitor.data.remote

import okhttp3.internal.http.toHttpDateString
import retrofit2.http.GET
import retrofit2.http.Query
import ru.spb.yakovlev.stocksmonitor.data.remote.responses.CompanyNewsResponse
import ru.spb.yakovlev.stocksmonitor.data.remote.responses.CompanyProfileResponse
import ru.spb.yakovlev.stocksmonitor.data.remote.responses.IndexResponse
import java.text.SimpleDateFormat
import java.util.*

interface RestService {


   //https://finnhub.io/api/v1/index/constituents?symbol=^GSPC&token=apikey
   @GET("index/constituents")
    suspend fun loadIndices(
       @Query("symbol") indexName: String = "^DJI"
    ):IndexResponse

   // https://finnhub.io/api/v1/stock/profile2?symbol=AAPL&token=c19sl0v48v6tl8v9p8fg
   @GET("index/constituents")
   suspend fun loadOCompanyProfile(
       @Query("symbol") ticker: String = "AAPL"
   ): CompanyProfileResponse

   // https://finnhub.io/api/v1/company-news?symbol=AAPL&from=2021-03-01&to=2021-03-09&token=c19sl0v48v6tl8v9p8fg
   @GET("company-news")
   suspend fun loadOCompanyNews(
       @Query("symbol") ticker: String = "AAPL",
       @Query("from") from: String = "AAPL",
       @Query("to") to: String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()),
   ): List<CompanyNewsResponse>

    //https://finnhub.io/api/v1/stock/candle?symbol=IBM&resolution=D&from=1572651390&to=1575243390&token=c19sl0v48v6tl8v9p8fg

    @GET("stostock/candleck/candle")
    suspend fun loadCandles(
        @Query("symbol") ticker: String = "AAPL",
        @Query("symbol") resolution: String = "1",
        @Query("from") from: Date,
        @Query("to") to: Date = Date(),
    ): List<CompanyNewsResponse>
}