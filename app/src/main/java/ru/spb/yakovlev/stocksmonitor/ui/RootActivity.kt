package ru.spb.yakovlev.stocksmonitor.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.spb.yakovlev.stocksmonitor.R
import ru.spb.yakovlev.stocksmonitor.model.interactors.RealTimePricesInteractor
import javax.inject.Inject

@AndroidEntryPoint
class RootActivity : AppCompatActivity(R.layout.activity_root) {
    lateinit var navController: NavController

    @Inject
    lateinit var realTimePricesInteractor: RealTimePricesInteractor

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        navController = findNavController(R.id.nav_host_fragment)
    }

    override fun onResume() {
        super.onResume()
        realTimePricesInteractor.start()
    }

    override fun onPause() {
        super.onPause()
        realTimePricesInteractor.stop()
    }
}