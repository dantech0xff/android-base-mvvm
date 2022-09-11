package com.creative.mvvm.ui.launch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.creative.mvvm.data.app.AppRepo
import com.creative.mvvm.ui.base.BaseViewModel

class XLauncherViewModel (val appRepo: AppRepo) : BaseViewModel() {
    private val _openDrawerLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val openDrawerLiveData: LiveData<Boolean> = _openDrawerLiveData

    fun openDrawer() {
        _openDrawerLiveData.postValue(true)
    }
}
