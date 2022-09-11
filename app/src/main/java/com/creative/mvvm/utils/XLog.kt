package com.creative.mvvm.utils

import android.util.Log
import com.creative.mvvm.BuildConfig

object XLog {
    fun d(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d("XLog", msg)
        }
    }

    fun e(throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            Log.e("XLog", throwable.stackTraceToString())
        }
    }
}