package com.creative.mvvm.google

import com.creative.mvvm.BuildConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings


object RemoteConfigHelper {
    fun fetch() {
        Firebase.remoteConfig.apply {
            setConfigSettingsAsync(remoteConfigSettings {
                minimumFetchIntervalInSeconds = 3600 * 24
            })
            fetchAndActivate()
        }
    }

    fun getInterstitialId(): String {
        val ret = Firebase.remoteConfig.getString("admob_interstitial_id")
        return when {
            BuildConfig.DEBUG -> "ca-app-pub-3940256099942544/1033173712"
            ret.isNotEmpty() -> ret
            else -> "ca-app-pub-2313206220567592/2176627382"
        }
    }

    fun getAppOpenAdId():String {
        val ret = Firebase.remoteConfig.getString("admob_app_open_id")
        return when {
            BuildConfig.DEBUG -> "ca-app-pub-3940256099942544/3419835294"
            ret.isNotEmpty() -> ret
            else -> "ca-app-pub-2313206220567592/5459439840"
        }
    }

    fun enableSelectColor(): Boolean {
        return Firebase.remoteConfig.getBoolean("enable_select_color")
    }
}