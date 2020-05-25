/*
 * Created by Weiyi Li on 2020-05-11.
 * https://github.com/li2
 */
package me.li2.movies.util

import me.li2.android.common.number.NumberFormatUtils
import me.li2.android.common.number.orZero

object NumberFormatter {

    fun formatRuntime(runtime: Int?): String {
        val hours = runtime.orZero() / 60
        val minutes = runtime.orZero() % 60
        return when {
            hours > 0 && minutes > 0 -> "${hours}hr ${minutes}min"
            hours > 0 && minutes == 0 -> "${hours}hr"
            hours == 0 && minutes > 0 -> "${minutes}min"
            else -> null
        }.orEmpty()
    }

    fun formatVoteCount(voteCount: Int): String {
        return NumberFormatUtils.formatNumber(voteCount.toDouble(), "###,###", ',')
    }
}