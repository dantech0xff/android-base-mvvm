package com.creative.mvvm.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {
    protected val disposableCollector = CompositeDisposable()

    val messageString: MutableLiveData<String> = MutableLiveData()
    val messageStringId: MutableLiveData<Int> = MutableLiveData()

    override fun onCleared() {
        super.onCleared()
        disposableCollector.clear()
    }
}