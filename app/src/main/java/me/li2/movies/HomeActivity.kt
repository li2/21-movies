package me.li2.movies

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.home_activity.*
import me.li2.android.view.navigation.setToolbar
import me.li2.android.view.system.hideStatusBar
import me.li2.movies.base.BaseActivity

class HomeActivity : BaseActivity(), LifecycleOwner, NavController.OnDestinationChangedListener {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        setToolbar(toolbar)
        hideStatusBar()
        initNavigation()
    }

    private fun initNavigation() {
        navController = (nav_host_fragment as NavHostFragment).navController
        navController.addOnDestinationChangedListener(this)
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    override fun onDestinationChanged(controller: NavController,
                                      destination: NavDestination,
                                      arguments: Bundle?) {
        when (destination.id) {
            R.id.homeFragment,
            R.id.movieDetailFragment -> supportActionBar?.hide()
            else -> setToolbar(toolbar)
        }
    }
}