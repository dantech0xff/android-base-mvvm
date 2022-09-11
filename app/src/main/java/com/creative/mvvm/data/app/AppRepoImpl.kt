package com.creative.mvvm.data.app

import android.app.Application
import khangtran.preferenceshelper.PrefHelper
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepoImpl @Inject constructor(val context: Application) : AppRepo {

    init {
        PrefHelper.initHelper(context, PrefHelperTAG)
    }

    private val appData: AppData by lazy {
        AppData.fromJson(JSONObject(PrefHelper.getStringVal(KEY_APP_DATA, "{}")))
    }

    companion object {
        private const val KEY_APP_DATA = "key_app_data"
        private const val PrefHelperTAG = "APP_DATA"

        private const val IS_REMOVE_ADS = "IS_REMOVE_ADS"
        private const val DEFAULT_IS_REMOVE_ADS = false
    }

    override fun appRemoveAds(): Boolean {
        return PrefHelper.getBooleanVal(IS_REMOVE_ADS, DEFAULT_IS_REMOVE_ADS)
    }

    override fun setRemoveAds(remove: Boolean) {
        PrefHelper.setVal(IS_REMOVE_ADS, remove)
    }

    override fun commit() {
        //TODO implement this if you don't like to use PrefHelper :D
    }
}