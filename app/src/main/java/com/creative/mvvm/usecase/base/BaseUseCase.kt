package com.creative.mvvm.usecase.base

import com.creative.mvvm.BuildConfig

abstract class BaseUseCase {
    open fun doOnError(throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            throwable.printStackTrace()
        }
    }
}