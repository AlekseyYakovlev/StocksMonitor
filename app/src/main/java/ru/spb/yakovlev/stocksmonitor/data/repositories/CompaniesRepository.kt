package ru.spb.yakovlev.stocksmonitor.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import ru.spb.yakovlev.stocksmonitor.data.local.daos.CompanyDao
import ru.spb.yakovlev.stocksmonitor.data.local.entites.CompanyEntity
import ru.spb.yakovlev.stocksmonitor.data.remote.FinHubRestService
import ru.spb.yakovlev.stocksmonitor.data.remote.responses.CompanyProfileResponse
import ru.spb.yakovlev.stocksmonitor.model.Company
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompaniesRepository @Inject constructor(
    private val finHubRest: FinHubRestService,
    private val companyDao: CompanyDao,
) {

    fun getCompanyByTickerFlow(ticker: String): Flow<Company> =
        companyDao.findCompanyByTickerFlow(ticker)
            .filterNotNull()
            .map { it.toCompany() }

    suspend fun getCompanyByTicker(ticker: String): Company {

        val dbRes = companyDao.findCompanyByTicker(ticker)
        if (dbRes != null) return dbRes.toCompany()

        val netRes = loadFromNet(ticker)
        netRes?.let {
            companyDao.insert(it.toCompanyEntity())
        }
        return companyDao.findCompanyByTicker(ticker)?.toCompany() ?: Company(ticker = ticker)
    }


    suspend fun getCompaniesByTickers(listOfTickers: List<String>): List<Company> {
        if (listOfTickers.isEmpty()) return emptyList()

        val dbRes = companyDao.findCompaniesByTickers(listOfTickers)
        if (dbRes.size == listOfTickers.size) return dbRes.map { it.toCompany() }
        else {
            val tempList = mutableListOf<CompanyEntity>()
            listOfTickers.forEach { ticker ->
                val netRes = loadFromNet(ticker)
                netRes?.let {
                    tempList.add(it.toCompanyEntity())
                }
            }

            companyDao.insert(tempList)
        }
        return companyDao.findCompaniesByTickers(listOfTickers).map { it.toCompany() }
    }

    private suspend fun loadFromNet(ticker: String): CompanyProfileResponse? =
        try {
            finHubRest.loadCompanyProfile(ticker)
        } catch (e: Exception) {
            Timber.tag("CompaniesRepository").e(e)
            null
        }
}

private fun CompanyEntity.toCompany() = Company(
    ticker = ticker,
    country = country,
    currency = currency,
    exchange = exchange,
    ipo = ipo,
    marketCapitalization = marketCapitalization,
    name = name,
    phone = phone,
    shareOutstanding = shareOutstanding,
    webUrl = webUrl,
    logo = logo,
    industry = industry,
    lastUpdated = lastUpdated,
)

private fun CompanyProfileResponse.toCompanyEntity() = CompanyEntity(
    ticker = ticker,
    country = country,
    currency = currency,
    exchange = exchange,
    ipo = ipo,
    marketCapitalization = marketCapitalization,
    name = name,
    phone = phone,
    shareOutstanding = shareOutstanding,
    webUrl = webUrl,
    logo = logo,
    industry = industry,
    lastUpdated = Date(),
)