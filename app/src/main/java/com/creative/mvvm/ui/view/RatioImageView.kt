package com.creative.mvvm.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.creative.mvvm.R

class RatioImageView : AppCompatImageView {

    companion object {
        const val DEFAULT_RATIO = 0.75F
    }

    var ratio: Float = DEFAULT_RATIO

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.RatioImageView).apply {
                ratio = getFloat(R.styleable.RatioImageView_ratio, DEFAULT_RATIO)
                recycle()
            }
        }
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.RatioImageView).apply {
                ratio = getFloat(R.styleable.RatioImageView_ratio, DEFAULT_RATIO)
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var width = measuredWidth
        var height = measuredHeight

        when {
            width > 0 -> height = (width * ratio).toInt()
            height > 0 -> width = (height / ratio).toInt()
            else -> return
        }

        setMeasuredDimension(width, height)
    }
}