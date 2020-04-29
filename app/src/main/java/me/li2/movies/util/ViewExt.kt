package me.li2.movies.util

import android.view.View
import android.widget.LinearLayout
import androidx.annotation.Px
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Emitter
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.Disposable
import me.li2.android.common.number.orZero
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

/**
 * An observable which emits value when RecyclerView was scrolled to bottom.
 */
fun RecyclerView.onScrolledBottom(): Observable<Unit> {

    class RecyclerViewScrollObservable(private val recyclerView: RecyclerView)
        : ObservableOnSubscribe<Unit>, Disposable {

        private lateinit var emitter: Emitter<Unit>
        private var onScrollListener: RecyclerView.OnScrollListener?

        init {
            onScrollListener = object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = layoutManager?.itemCount.orZero()
                    val visibleItemCount = layoutManager?.childCount.orZero()
                    val firstVisibleItemPosition = (layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition().orZero()
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount) {
                        emitter.onNext(Unit)
                    }
                }
            }
        }

        override fun subscribe(emitter: ObservableEmitter<Unit>) {
            this.emitter = emitter
            onScrollListener?.let {
                recyclerView.addOnScrollListener(it)
            }
        }

        override fun dispose() {
            onScrollListener?.let {
                recyclerView.removeOnScrollListener(it)
            }
        }

        override fun isDisposed() = onScrollListener != null
    }

    return Observable.create(RecyclerViewScrollObservable(this))
}