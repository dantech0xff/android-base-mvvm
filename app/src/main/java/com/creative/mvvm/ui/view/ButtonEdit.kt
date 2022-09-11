package com.creative.mvvm.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.creative.mvvm.databinding.LayoutButtonEditBinding

class ButtonEdit : CardView {

    private val _viewBinding: LayoutButtonEditBinding =
        LayoutButtonEditBinding.inflate(LayoutInflater.from(context), this, true)
    val viewBinding = _viewBinding

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )
}