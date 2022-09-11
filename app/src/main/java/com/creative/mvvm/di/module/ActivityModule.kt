package com.creative.mvvm.di.module

import androidx.lifecycle.ViewModelProvider
import com.creative.mvvm.data.app.AppRepo
import com.creative.mvvm.factory.viewModelFactory
import com.creative.mvvm.ui.base.BaseActivity
import com.creative.mvvm.ui.launch.XLauncherViewModel
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: BaseActivity<*,*>) {

    @Provides
    fun provideXLauncherViewModel(appRepo: AppRepo): XLauncherViewModel =
        ViewModelProvider(
            activity,
            viewModelFactory { XLauncherViewModel(appRepo) })[XLauncherViewModel::class.java]
}