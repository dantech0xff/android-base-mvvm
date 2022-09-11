package com.creative.mvvm.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.creative.mvvm.databinding.LayoutXToolbarBinding

class XToolbar : FrameLayout {
    private var _viewBinding: LayoutXToolbarBinding =
        LayoutXToolbarBinding.inflate(LayoutInflater.from(context), this, true)

    private var _clickListener: ClickListener? = null

    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr)

    fun setToolbarClickListener(clickListener: ClickListener) {
        _clickListener = clickListener
        _viewBinding.apply {
            buttonMenuDrawer.setOnClickListener {
                _clickListener?.onDrawerClick()
            }
            buttonRightMenu.setOnClickListener {
                _clickListener?.onMenuRightClick()
            }
        }
    }

    fun setDrawerButtonResource(resId: Int) {
        _viewBinding.buttonMenuDrawer.setImageResource(resId)
    }
    fun setDrawerButtonVisible(visible: Int) {
        _viewBinding.buttonMenuDrawer.visibility = visible
    }

    fun setRightMenuButtonResource(resId: Int) {
        _viewBinding.buttonRightMenu.setImageResource(resId)
    }
    fun setRightMenuButtonVisible(visible: Int) {
        _viewBinding.buttonRightMenu.visibility = visible
    }

    fun setTitleTextVisible(visible: Int) {
        _viewBinding.title.visibility = visible
    }
    fun setTitleText(titleResId: Int) {
        _viewBinding.title.text = context.getString(titleResId)
    }
    fun setTitleColor(titleResColorId: Int) {
        _viewBinding.title.setTextColor(context.getColor(titleResColorId))
    }

    interface ClickListener {
        fun onDrawerClick()
        fun onMenuRightClick()
    }
}