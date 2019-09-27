package me.li2.movies

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import me.li2.movies.base.BaseActivity

class MainActivity : BaseActivity(), LifecycleOwner, NavController.OnDestinationChangedListener {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolBar()
        initNavigation()
    }

    private fun initToolBar() {
        setSupportActionBar(toolbar)
        toolbar.visibility = View.VISIBLE
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    private fun initNavigation() {
        navController = (nav_host_fragment as NavHostFragment).navController
        navController.addOnDestinationChangedListener(this)
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        when (destination.id) {
        }
    }
}
