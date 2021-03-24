package ru.spb.yakovlev.stocksmonitor.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import ru.spb.yakovlev.stocksmonitor.R
import ru.spb.yakovlev.stocksmonitor.databinding.ActivityRootBinding

@AndroidEntryPoint
class RootActivity : AppCompatActivity(R.layout.activity_root) {
    private val vb by viewBinding(ActivityRootBinding::bind, R.id.root_container)

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        //val navView: BottomNavigationView = findViewById(R.id.nav_view)
//
//        val navController = findNavController(R.id.nav_host_fragment)
//
//
//
////        if (savedInstanceState == null) {
////            supportFragmentManager.beginTransaction()
////                    .replace(R.id.container, MainFragment.newInstance())
////                    .commitNow()
////        }
//
//    }
}