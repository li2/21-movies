/*
 * Created by Weiyi on 2020-05-27.
 * https://github.com/li2
 */
package me.li2.movies.ui.filter

import android.content.Context
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButtonToggleGroup
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import me.li2.movies.R
import me.li2.movies.ui.filter.ReleaseDateFilter.*
import kotlin.math.ceil
import kotlin.math.floor

class FilterBottomSheet @JvmOverloads constructor(
        context: Context,
        defStyleAttr: Int = 0,
        initialFilter: MoviesFilter,
        onApplyClick: (MoviesFilter) -> Unit) : BottomSheetDialog(context, defStyleAttr) {

    private var filter = initialFilter
    private var releaseDateGroup: MaterialButtonToggleGroup
    private var voteSeekbar: RangeSeekBar
    private var cancelButton: Button
    private var applyButton: Button

    init {
        setContentView(R.layout.filter_bottom_sheet)
        dismissWithAnimation = true

        releaseDateGroup = findViewById(R.id.releaseDateButtonGroup)!!
        voteSeekbar = findViewById(R.id.voteSeekbar)!!
        cancelButton = findViewById(R.id.cancelButton)!!
        applyButton = findViewById(R.id.applyButton)!!

        val checkedButtonId = when (filter.releaseDateFilter) {
            ALL -> R.id.buttonAll
            WITHIN_1_MONTH -> R.id.button1M
            WITHIN_3_MONTHS -> R.id.button3M
            WITHIN_1_YEAR -> R.id.button1Year
        }
        releaseDateGroup.check(checkedButtonId)
        releaseDateGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                filter.releaseDateFilter = when (checkedId) {
                    R.id.button1M -> WITHIN_1_MONTH
                    R.id.button3M -> WITHIN_3_MONTHS
                    R.id.button1Year -> WITHIN_1_YEAR
                    else -> ALL
                }
            }
        }

        voteSeekbar.setProgress(filter.voteRange.first.toFloat(), filter.voteRange.last.toFloat())
        voteSeekbar.setOnRangeChangedListener(object : OnRangeChangedListener {
            override fun onRangeChanged(view: RangeSeekBar?, leftValue: Float, rightValue: Float, isFromUser: Boolean) {
                filter.voteRange = IntRange(ceil(leftValue).toInt(), floor(rightValue).toInt())
            }

            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
            }

        })

        cancelButton.setOnClickListener {
            dismiss()
        }

        applyButton.setOnClickListener {
            dismiss()
            onApplyClick(filter)
        }
    }
}