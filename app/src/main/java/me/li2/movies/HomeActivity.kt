/*
 * Created by Weiyi Li on 2019-09-28.
 * https://github.com/li2
 */
package me.li2.movies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import me.li2.android.view.navigation.setToolbar
import me.li2.android.view.system.hideStatusBar
import me.li2.movies.base.BaseActivity
import me.li2.movies.data.model.MapperUI
import me.li2.movies.databinding.HomeActivityBinding
import me.li2.movies.fcm.NotificationUtil.dispatchPushNotification
import me.li2.movies.ui.home.HomeFragmentDirections

class HomeActivity : BaseActivity(), LifecycleOwner, NavController.OnDestinationChangedListener {

    private lateinit var binding: HomeActivityBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = HomeActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        // set toolbar for navController
        setToolbar(binding.toolbar)
        // hide toolbar throughout the App because each fragment will be responsible to set up own toolbar.
        supportActionBar?.hide()
        // hide statusBar throughout the App because most fragments need to draw image behind statusBar,
        // to avoid statusBar flick(show/hide) when navigate between screens.
        // for other fragments just add a margin on top of toolbar, or android:fitsSystemWindows="true"
        hideStatusBar()
        initNavigation()

        checkPushNotification(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        checkPushNotification(intent)
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
    }

    private fun checkPushNotification(intent: Intent?) {
        dispatchPushNotification(intent) { message ->
            val movie = MapperUI.toMovieItemUI(message)
            navController.navigate(HomeFragmentDirections.showMovieDetail(movie))
        }
    }
}
