package me.li2.movies

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.home_activity.*
import me.li2.android.view.navigation.setToolbar
import me.li2.android.view.system.hideStatusBar
import me.li2.android.view.system.showStatusBar
import me.li2.movies.base.BaseActivity
import me.li2.movies.databinding.HomeActivityBinding

class HomeActivity : BaseActivity(), LifecycleOwner, NavController.OnDestinationChangedListener {

    private lateinit var binding: HomeActivityBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = HomeActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setToolbar(binding.toolbar)
        hideStatusBar()
        initNavigation()
    }

    private fun initNavigation() {
        navController = findNavController(R.id.navHostFragment)
        navController.addOnDestinationChangedListener(this)
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    override fun onDestinationChanged(controller: NavController,
                                      destination: NavDestination,
                                      arguments: Bundle?) {
        when (destination.id) {
            R.id.homeFragment,
            R.id.movieDetailFragment -> {
                supportActionBar?.hide()
                hideStatusBar()
            }
            else -> {
                setToolbar(toolbar)
                supportActionBar?.show()
                showStatusBar()
            }
        }
    }
}
