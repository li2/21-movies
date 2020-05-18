package me.li2.movies.ui.movies

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import me.li2.movies.R
import me.li2.movies.ui.widgets.movies.MovieViewHolder
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class MoviesFragmentTest {

    /**
     * Test navigation
     * https://developer.android.com/guide/navigation/navigation-testing
     * https://developer.android.com/training/testing/espresso/lists#recycler-view-list-items
     * https://stackoverflow.com/questions/44005338/android-espresso-performexception
     * https://stackoverflow.com/questions/56558775/launchfragmentincontainer-unable-to-resolve-activity-in-android
     */
    @Test
    fun testNavigationToMovieDetailScreen() {
        // Create a mock NavController
        val mockNavController = mock(NavController::class.java)

        // Create a graphical FragmentScenario for the movies list screen
        val moviesScenario = launchFragmentInContainer<MoviesFragment>()

        // Set the NavController property on the fragment
        moviesScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }

        // Verify that performing a click prompts the correct Navigation action
        onView(withId(R.id.moviesRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition<MovieViewHolder>(0, click()))
        // todo IllegalStateException: No view holder at position: 0
        verify(mockNavController).navigate(R.id.showMovieDetail)
    }
}