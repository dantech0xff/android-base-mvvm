package com.creative.mvvm.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.creative.mvvm.R

class RatioRecyclerView : RecyclerView {

    companion object {
        const val DEFAULT_RATIO = 0.7f
    }

    var ratio: Float = DEFAULT_RATIO

    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        attributeSet?.let {
            context.obtainStyledAttributes(it, R.styleable.RatioRecyclerView).apply {
                ratio = getFloat(R.styleable.RatioRecyclerView_rv_ratio, RatioImageView.DEFAULT_RATIO)
                recycle()
            }
        }
    }
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        attributeSet?.let {
            context.obtainStyledAttributes(it, R.styleable.RatioRecyclerView).apply {
                ratio = getFloat(R.styleable.RatioRecyclerView_rv_ratio, RatioImageView.DEFAULT_RATIO)
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