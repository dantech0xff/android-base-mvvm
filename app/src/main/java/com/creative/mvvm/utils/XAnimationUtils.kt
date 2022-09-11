package com.creative.mvvm.utils

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.creative.mvvm.R

object XAnimationUtils {

    fun fadeInView(
        view: View, listener: Animation.AnimationListener = object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
        }
    ) {
        view.apply {
            visibility = View.VISIBLE
            animation?.cancel()
            startAnimation(
                AnimationUtils.loadAnimation(view.context, R.anim.fade_in_anim).apply {
                    setAnimationListener(listener)
                })
        }
    }

    fun fadeInView(
        view: View,
        inDuration: Long,
        listener: Animation.AnimationListener = object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
        }
    ) {
        view.apply {
            visibility = View.VISIBLE
            animation?.cancel()
            startAnimation(
                AnimationUtils.loadAnimation(view.context, R.anim.fade_in_anim).apply {
                    setAnimationListener(listener)
                    duration = inDuration
                }
            )
        }
    }

    fun fadeOutView(view: View) {
        when (view.visibility) {
            View.VISIBLE -> {
                view.apply {
                    animation?.cancel()
                    startAnimation(
                        AnimationUtils.loadAnimation(view.context, R.anim.fade_out_anim).apply {
                            setAnimationListener(object : Animation.AnimationListener {
                                override fun onAnimationStart(p0: Animation?) {}

                                override fun onAnimationEnd(p0: Animation?) {
                                    visibility = View.GONE
                                }

                                override fun onAnimationRepeat(p0: Animation?) {}
                            })
                        })
                }
            }
            View.GONE -> {
            }
            View.INVISIBLE -> {
            }
        }
    }

    fun fadeOutView(view: View, listener: Animation.AnimationListener) {
        when (view.visibility) {
            View.VISIBLE -> {
                view.apply {
                    animation?.cancel()
                    startAnimation(
                        AnimationUtils.loadAnimation(view.context, R.anim.fade_out_anim).apply {
                            setAnimationListener(listener)
                        })
                }
            }
            View.GONE -> {
            }
            View.INVISIBLE -> {
            }
        }
    }

    fun fadeOutView(view: View, inDuration: Long, listener: Animation.AnimationListener) {
        when (view.visibility) {
            View.VISIBLE -> {
                view.apply {
                    visibility = View.VISIBLE
                    animation?.cancel()
                    startAnimation(
                        AnimationUtils.loadAnimation(view.context, R.anim.fade_out_anim).apply {
                            setAnimationListener(listener)
                            duration = inDuration
                        }
                    )
                }
            }
            View.GONE -> {
            }
            View.INVISIBLE -> {
            }
        }
    }
}