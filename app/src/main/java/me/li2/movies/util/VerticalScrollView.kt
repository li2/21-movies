package me.li2.movies.util

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView
import kotlin.math.abs

/**
 * A scroll view will only intercept the event if the user is deliberately scrolling
 * in the Y direction, to make the horizontal scroll view scroll smooth.
 *
 * @see <a href="https://stackoverflow.com/a/7703740/2722270">HorizontalScrollView within ScrollView Touch Handling</a>
 */
class VerticalScrollView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : NestedScrollView(context, attrs, defStyleAttr) {

    private var xDistance = 0f
    private var yDistance = 0f
    private var lastX = 0f
    private var lastY = 0f

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                xDistance = 0f
                yDistance = 0f
                lastX = ev.x
                lastY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                val curX = ev.x
                val curY = ev.y
                xDistance += abs(curX - lastX)
                yDistance += abs(curY - lastY)
                lastX = curX
                lastY = curY
                if (xDistance > yDistance) return false
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}