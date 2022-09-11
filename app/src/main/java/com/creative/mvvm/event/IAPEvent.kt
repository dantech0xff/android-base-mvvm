package com.creative.mvvm.event

class IAPEvent (val state: State) {
    enum class State {
        CANCEL, REMOVE_ADS_SUCCESS, ERROR
    }
}