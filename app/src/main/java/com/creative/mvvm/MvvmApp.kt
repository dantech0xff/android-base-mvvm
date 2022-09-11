package com.creative.mvvm

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import com.creative.mvvm.data.app.AppRepo
import com.creative.mvvm.di.component.ApplicationComponent
import com.creative.mvvm.di.component.DaggerApplicationComponent
import com.creative.mvvm.di.module.ApplicationModule
import com.creative.mvvm.google.AppOpenManager
import com.creative.mvvm.utils.XLog
import com.google.android.gms.ads.MobileAds
import javax.inject.Inject


class MvvmApp : MultiDexApplication() {

    lateinit var applicationComponent: ApplicationComponent

    @Inject
    lateinit var appOpenManager : AppOpenManager

    private fun injectDependencies() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
        applicationComponent.inject(this)
    }

    override fun onCreate() {
        super.onCreate()
        injectDependencies()
        MobileAds.initialize(this)
        registerActivityLifecycleCallbacks(appOpenManager)
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleObserver{
            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onStart() {
                XLog.d("Application onStart LifecycleObserver")
                appOpenManager.showAdIfAvailable()
            }
        })
    }
}