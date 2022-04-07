package com.armutyus.videogamesproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.armutyus.videogamesproject.databinding.ActivityMainBinding
import com.armutyus.videogamesproject.di.AppModule
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        val entryPoint = EntryPointAccessors.fromActivity(
            this,
            AppModule.GamesFragmentFactoryEntryPoint::class.java
        )
        supportFragmentManager.fragmentFactory = entryPoint.getFragmentFactory()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.favoritesFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setOnItemSelectedListener { item ->
            if (item.itemId != navView.selectedItemId) {
                navController.navigate(item.itemId)
            }
            true
        }

        navView.setOnItemReselectedListener { selectedItem ->
            if (selectedItem.itemId == navView.selectedItemId) {
                navController.navigate(navView.selectedItemId)
            }
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}