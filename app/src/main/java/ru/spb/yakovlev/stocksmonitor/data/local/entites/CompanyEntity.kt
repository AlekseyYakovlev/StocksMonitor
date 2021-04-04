package ru.spb.yakovlev.stocksmonitor.data.local.entites

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class CompanyEntity(
   @PrimaryKey
   val ticker: String = "",
   val country: String = "",
   val currency: String = "USD",
   val exchange: String = "",
   val ipo: String = "", //date
   val marketCapitalization: Double = 0.0,
   val name: String = "",
   val phone: String = "",
   val shareOutstanding: Double = 0.0,
   val webUrl: String = "",
   val logo: String = "",
   val industry: String = "",
   val lastUpdated: Date = Date(),
)