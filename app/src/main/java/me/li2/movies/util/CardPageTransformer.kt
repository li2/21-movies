package me.li2.movies.util

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class CardPageTransformer(private val pageScale: Float = 0.9f,
                          private val pageAlpha: Float = 0.5f): ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val scale: Float = if (position < 0) (1 - pageScale) * position + 1 else (pageScale - 1) * position + 1
        val alpha: Float = if (position < 0) (1 - pageAlpha) * position + 1 else (pageAlpha - 1) * position + 1
        if (position < 0) {
            page.pivotX = page.width.toFloat()
            page.pivotY = (page.height / 2).toFloat()
        } else {
            page.pivotX = 0f
            page.pivotY = (page.height / 2).toFloat()
        }
        page.scaleX = scale
        page.scaleY = scale
        page.alpha = abs(alpha)
    }
}