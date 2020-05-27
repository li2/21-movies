/*
 * Created by Weiyi Li on 2020-04-26.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import me.li2.movies.R
import me.li2.movies.data.model.GenreUI

class GenresGroupView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : ChipGroup(context, attrs, defStyleAttr) {

    private var labelTextView: TextView
    private var chipGroup: ChipGroup
    private val itemClicksSubject: PublishSubject<GenreUI> = PublishSubject.create()

    var label: String? = null
        set(value) {
            field = value
            labelTextView.text = value
        }

    var genres: List<GenreUI>? = emptyList()
        set(value) {
            field = value
            chipGroup.removeAllViews()
            value?.forEach { genre ->
                chipGroup.addView(createGenreChip(genre))
            }
        }

    init {
        // set attachToRoot to true otherwise view won't show. 2note
        val view = LayoutInflater.from(context).inflate(R.layout.genres_group_view, this, true)
        labelTextView = view.findViewById(R.id.labelTextView)
        chipGroup = view.findViewById(R.id.scrollChipGroup)
    }

    fun genreClicks(): Observable<GenreUI> = itemClicksSubject.toFlowable(BackpressureStrategy.LATEST).toObservable()

    private fun createGenreChip(genre: GenreUI): Chip {
        return (LayoutInflater.from(context).inflate(R.layout.genre_chip_view, chipGroup, false) as Chip).apply {
            text = genre.name
            setOnCloseIconClickListener {
                itemClicksSubject.onNext(genre)
            }
            setOnClickListener {
                itemClicksSubject.onNext(genre)
            }
        }
    }
}
