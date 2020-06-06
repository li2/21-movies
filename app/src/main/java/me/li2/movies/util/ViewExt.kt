/*
 * Created by Weiyi Li on 2020-04-25.
 * https://github.com/li2
 */
package me.li2.movies.util

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.doOnLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.textfield.TextInputLayout
import com.rd.PageIndicatorView
import io.reactivex.rxjava3.core.Observable

fun PageIndicatorView.setViewPager2(viewPager2: ViewPager2) {
    val adapter = (viewPager2.adapter as? ListAdapter<*, *>) ?: return

    viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            val realPosition = position % adapter.currentList.size
            setSelected(realPosition)
        }
    })

    // not working when submitList
    adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            count = adapter.itemCount
        }
    })
}

fun EditText.imeActionSearchClicks(): Observable<Unit> {
    return Observable.create { emitter ->
        setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                emitter.onNext(Unit)
                true
            } else {
                false
            }
        }
    }
}

fun TextInputLayout.endIconClicks(): Observable<Unit> {
    return Observable.create { emitter ->
        setEndIconOnClickListener {
            emitter.onNext(Unit)
        }
    }
}

fun SearchView.init(menuItem: MenuItem,
                    query: String,
                    hint: String,
                    onQueryTextSubmit: (String?) -> Unit) {
    this.queryHint = hint
    this.isIconified = false
    // Expand SearchView Without Focus (Hide Keyboard)
    menuItem.expandActionView()
    this.doOnLayout { this.clearFocus() }
    // Show query (only works after expandActionView)
    this.setQuery(query, false)
    // Only query when keyboard actionSearch clicks
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            onQueryTextSubmit(query)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    })
}

// https://stackoverflow.com/questions/2734270/how-to-make-links-in-a-textview-clickable
@Suppress("DEPRECATION")
@BindingAdapter("html")
fun setHtmlText(textView: TextView, stringRes: String) {
    fun fromHtmlCompat(source: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(source)
        }
    }
    textView.text = fromHtmlCompat(stringRes)
    textView.movementMethod = LinkMovementMethod.getInstance()
}