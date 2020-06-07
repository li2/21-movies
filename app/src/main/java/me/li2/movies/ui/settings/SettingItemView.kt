/*
 * Created by Weiyi Li on 2020-06-05.
 * https://github.com/li2
 */
package me.li2.movies.ui.settings

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import me.li2.movies.R

class SettingItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var startIconImageView: ImageView
    private var endIconImageView: ImageView
    private var titleTextView: TextView
    private var valueTextView: TextView

    var settingTitle: String? = null
        set(value) {
            field = value
            titleTextView.text = value
        }

    var settingValue: String? = null
        set(value) {
            field = value
            valueTextView.text = value
        }

    init {
        View.inflate(context, R.layout.setting_item_view, this)

        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.SettingItemViewAttrs, 0, 0)
        val _backgroundColor = attributes.getColor(R.styleable.SettingItemViewAttrs_settingBackgroundColor, Color.WHITE)
        val _startIcon = attributes.getDrawable(R.styleable.SettingItemViewAttrs_settingStartIcon)
        val _endIcon = attributes.getDrawable(R.styleable.SettingItemViewAttrs_settingEndIcon)
        val _iconTintColor = attributes.getColorStateList(R.styleable.SettingItemViewAttrs_settingIconTint)
        val _title = attributes.getString(R.styleable.SettingItemViewAttrs_settingTitle)
        val _titleTextAppearance = attributes.getResourceId(R.styleable.SettingItemViewAttrs_settingTitleTextAppearance, android.R.style.TextAppearance_Material_Title)
        val _titleTextColor = attributes.getColorStateList(R.styleable.SettingItemViewAttrs_settingTitleTextColor)
        val _value = attributes.getString(R.styleable.SettingItemViewAttrs_settingValue)
        val _valueTextAppearance = attributes.getResourceId(R.styleable.SettingItemViewAttrs_settingValueTextAppearance, android.R.style.TextAppearance_Material_Body2)
        val _valueTextColor = attributes.getColorStateList(R.styleable.SettingItemViewAttrs_settingValueTextColor)
        attributes.recycle()

        startIconImageView = findViewById(R.id.startIconImageView)
        endIconImageView = findViewById(R.id.endIconImageView)
        titleTextView = findViewById(R.id.titleTextView)
        valueTextView = findViewById(R.id.valueTextView)

        setBackgroundColor(_backgroundColor)
        startIconImageView.setImageDrawable(_startIcon)
        endIconImageView.setImageDrawable(_endIcon)
        endIconImageView.isVisible = _endIcon != null
        _iconTintColor?.let {
            startIconImageView.imageTintList = it
            endIconImageView.imageTintList = it
        }
        titleTextView.apply {
            text = _title
            setTextAppearance(context, _titleTextAppearance)
            _titleTextColor?.let { setTextColor(it) }
        }
        valueTextView.apply {
            text = _value
            setTextAppearance(context, _valueTextAppearance)
            _valueTextColor?.let { setTextColor(it) }
        }
    }
}