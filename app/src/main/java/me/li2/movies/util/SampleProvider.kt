/*
 * Created by Weiyi Li on 2020-05-18.
 * https://github.com/li2
 */
package me.li2.movies.util

import me.li2.movies.data.model.GenreUI
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.ui.movies.GenreCategory

object SampleProvider {
    fun movieItemList() = listOf(
            MovieItemUI(1, "Title", null, "2020-05-20", null, null, null, 50.0, 6.0, "6.0", 1000, "1,000", "overview"),
            MovieItemUI(2, "Title", null, "2020-05-20", null, null, null, 50.0, 6.0, "6.0", 1000, "1,000", "overview"),
            MovieItemUI(3, "Title", null, "2020-05-20", null, null, null, 50.0, 6.0, "6.0", 1000, "1,000", "overview"),
            MovieItemUI(4, "Title", null, "2020-05-20", null, null, null, 50.0, 6.0, "6.0", 1000, "1,000", "overview"),
            MovieItemUI(5, "Title", null, "2020-05-20", null, null, null, 50.0, 6.0, "6.0", 1000, "1,000", "overview")
    )

    fun categoryList() = listOf(
            GenreCategory(GenreUI(28, "Action")),
            GenreCategory(GenreUI(12, "Adventure")),
            GenreCategory(GenreUI(16, "Animation")),
            GenreCategory(GenreUI(35, "Comedy")),
            GenreCategory(GenreUI(80, "Crime")),
            GenreCategory(GenreUI(99, "Documentary")),
            GenreCategory(GenreUI(18, "Drama")),
            GenreCategory(GenreUI(14, "Fantasy"))
    )
}