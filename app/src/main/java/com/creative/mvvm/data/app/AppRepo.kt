package com.creative.mvvm.data.app

interface AppRepo {
    fun appRemoveAds(): Boolean
    fun setRemoveAds(remove: Boolean)
    fun commit()
}