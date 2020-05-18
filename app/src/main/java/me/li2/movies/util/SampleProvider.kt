/*
 * Created by Weiyi Li on 2020-05-18.
 * https://github.com/li2
 */
package me.li2.movies.util

import me.li2.movies.data.model.MovieItemUI

object SampleProvider {
    fun movieItemList() = listOf(
            MovieItemUI(1, "Title", null, "2020-05-20", null, null, 50.0, 6.0, "6.0", 1000, "1,000", "overview"),
            MovieItemUI(2, "Title", null, "2020-05-20", null, null, 50.0, 6.0, "6.0", 1000, "1,000", "overview"),
            MovieItemUI(3, "Title", null, "2020-05-20", null, null, 50.0, 6.0, "6.0", 1000, "1,000", "overview"),
            MovieItemUI(4, "Title", null, "2020-05-20", null, null, 50.0, 6.0, "6.0", 1000, "1,000", "overview"),
            MovieItemUI(5, "Title", null, "2020-05-20", null, null, 50.0, 6.0, "6.0", 1000, "1,000", "overview")
    )
}