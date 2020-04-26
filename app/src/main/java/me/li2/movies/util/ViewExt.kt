package me.li2.movies.util

import android.view.View
import android.widget.LinearLayout
import androidx.annotation.Px
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import me.li2.android.view.popup.snackbar
import me.li2.movies.R

fun Snackbar.colorBackgroundFloating() {
    setBackgroundTint(context.getColorFromAttr(R.attr.colorBackgroundFloating))
}

fun View.setMargins(@Px left: Int, @Px top: Int, @Px right: Int, @Px bottom: Int) {
    (this.layoutParams as? LinearLayout.LayoutParams)?.setMargins(left, top, right, bottom)
    this.requestLayout()
}

fun Fragment.themedSnackbar(content: String) {
    // todo weiyi set margin not working
//    Snackbar.make(requireActivity().findViewById<View>(android.R.id.content), content, Toast.LENGTH_SHORT)
//            .apply {
//                this.colorBackgroundFloating()
//                val margin = 48.dpToPx(context)
//                this.view.setMargins(margin, margin, margin, margin)
//                show()
//            }
    snackbar(content)?.apply {
        this.colorBackgroundFloating()
    }
}