package me.li2.movies.util

import android.content.Context
import androidx.annotation.Px

@Px
fun Int.dpToPx(context: Context): Int = (this * context.resources.displayMetrics.density).toInt()

@Px
fun Int.pxToDp(context: Context): Int = (this / context.resources.displayMetrics.density).toInt()