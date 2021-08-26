package me.li2.movies.ui.movies

import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import me.li2.movies.R
import me.li2.movies.ui.widgets.movies.MovieViewHolder
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MoviesFragmentTest {

    @IdRes
    private val theme: Int = R.style.AppTheme

    /**
     * Test navigation
     * https://developer.android.com/guide/navigation/navigation-testing
     * https://developer.android.com/training/testing/espresso/lists#recycler-view-list-items
     * https://stackoverflow.com/questions/44005338/android-espresso-performexception
     * https://stackoverflow.com/questions/56558775/launchfragmentincontainer-unable-to-resolve-activity-in-android
     */
    @Test
    fun testMoviesFragmentToMovieDetailScreen() {
        // Create a TestNavHostController
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        // Create a graphical FragmentScenario for the movies list screen
        val args = bundleOf("moviesCategory" to NowPlayingCategory)
        val moviesScenario = launchFragmentInContainer<MoviesFragment>(args, theme)

        // Set the NavController property on the fragment
        moviesScenario.onFragment { fragment ->
            // Set the graph on the TestNavHostController
            navController.setGraph(R.navigation.nav_main)

            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        // Verify that performing a click prompts the correct Navigation action
        onView(withId(R.id.moviesRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition<MovieViewHolder>(0, click()))

        assertEquals(navController.currentDestination?.id, R.id.movieDetailFragment)
        //verify(navController).navigate(R.id.showMovieDetail)
    }
}