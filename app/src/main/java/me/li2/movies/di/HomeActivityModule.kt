package me.li2.movies.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.li2.movies.HomeActivity
import me.li2.movies.ui.discover.DiscoverFragment
import me.li2.movies.ui.home.HomeFragment
import me.li2.movies.ui.moviedetail.MovieDetailFragment
import me.li2.movies.ui.movies.MoviesFragment
import me.li2.movies.ui.settings.SettingsFragment
import me.li2.movies.ui.widgets.image.BigImageFragment

@Module
abstract class HomeActivityModule {

    @ContributesAndroidInjector
    abstract fun injectHomeActivity(): HomeActivity

    @ContributesAndroidInjector
    abstract fun injectBigImageFragment(): BigImageFragment

    @ContributesAndroidInjector
    abstract fun injectDiscoverFragment(): DiscoverFragment

    @ContributesAndroidInjector
    abstract fun injectHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun injectMovieDetailFragment(): MovieDetailFragment

    @ContributesAndroidInjector
    abstract fun injectMoviesFragment(): MoviesFragment

    @ContributesAndroidInjector
    abstract fun injectSettingsFragment(): SettingsFragment
}