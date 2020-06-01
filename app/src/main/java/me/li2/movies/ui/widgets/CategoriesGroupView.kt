/*
 * Created by Weiyi Li on 2020-04-26.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.subjects.PublishSubject
import me.li2.movies.R
import me.li2.movies.ui.movies.MoviesCategory
import me.li2.movies.util.SampleProvider

class CategoriesGroupView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : ChipGroup(context, attrs, defStyleAttr) {

    private var labelTextView: TextView
    private var chipGroup: ChipGroup
    private val _onCategoryClicks: PublishSubject<MoviesCategory> = PublishSubject.create()
    val onCategoryClicks = _onCategoryClicks.toFlowable(BackpressureStrategy.LATEST).toObservable()!!

    var label: String? = null
        set(value) {
            field = value
            labelTextView.text = value
            labelTextView.isVisible = !value.isNullOrEmpty()
        }

    var categories: List<MoviesCategory>? = emptyList()
        set(value) {
            field = value
            chipGroup.removeAllViews()
            value?.forEach { category ->
                chipGroup.addView(createChipView(category))
            }
        }

    init {
        // set attachToRoot to true otherwise view won't show. 2note
        val view = LayoutInflater.from(context).inflate(R.layout.categories_group_view, this, true)
        labelTextView = view.findViewById(R.id.labelTextView)
        chipGroup = view.findViewById(R.id.chipGroup)

        showSampleData()
    }

    private fun createChipView(category: MoviesCategory): Chip {
        return (LayoutInflater.from(context).inflate(R.layout.category_chip_view, chipGroup, false) as Chip).apply {
            text = category.label
            setOnCloseIconClickListener {
                _onCategoryClicks.onNext(category)
            }
            setOnClickListener {
                _onCategoryClicks.onNext(category)
            }
        }
    }

    /** show sample data when shown in the IDE preview */
    private fun showSampleData() {
        if (isInEditMode) {
            label = "Genres"
            categories = SampleProvider.categoryList()
        }
    }
}
