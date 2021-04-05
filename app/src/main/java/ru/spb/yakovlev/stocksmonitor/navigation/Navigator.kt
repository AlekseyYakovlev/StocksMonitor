package ru.spb.yakovlev.stocksmonitor.navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import ru.spb.yakovlev.stocksmonitor.R
import ru.spb.yakovlev.stocksmonitor.navigation.Destination.MAIN_FRAGMENT
import ru.spb.yakovlev.stocksmonitor.navigation.Destination.TICKER_DETAILS_FRAGMENT
import ru.spb.yakovlev.stocksmonitor.ui.details.DetailsFragmentArgs
import javax.inject.Inject

class Navigator @Inject constructor(
     val activity: AppCompatActivity
) {


    private val navController by lazy(::initNavController)

    fun back() {
        activity.onBackPressedDispatcher.onBackPressed()
    }

    fun goTo(destination: Destination, vararg args: String) {
        when (destination) {
            TICKER_DETAILS_FRAGMENT ->{
                navController.navigate(
                    R.id.action_mainFragment_to_detailsFragment,
                    DetailsFragmentArgs(args[0],args[1]).toBundle()
                )
            }
            MAIN_FRAGMENT -> navController.navigate(R.id.mainFragment)
        }
    }

    private fun initNavController(): NavController =
        activity.findNavController(R.id.nav_host_fragment)
}

enum class Destination(
) {
    TICKER_DETAILS_FRAGMENT,
    MAIN_FRAGMENT,
}