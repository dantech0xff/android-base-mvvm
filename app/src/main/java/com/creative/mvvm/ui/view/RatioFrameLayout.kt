package com.creative.mvvm.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.creative.mvvm.R

class RatioFrameLayout : FrameLayout {

    companion object {
        const val DEFAULT_RATIO = 1f
    }

    var ratio: Float = DEFAULT_RATIO

    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        attributeSet?.let {
            context.obtainStyledAttributes(it, R.styleable.RatioFrameLayout).apply {
                ratio = getFloat(R.styleable.RatioFrameLayout_fl_ratio, DEFAULT_RATIO)
                recycle()
            }
        }
    }
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        attributeSet?.let {
            context.obtainStyledAttributes(it, R.styleable.RatioFrameLayout).apply {
                ratio = getFloat(R.styleable.RatioFrameLayout_fl_ratio, DEFAULT_RATIO)
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