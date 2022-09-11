package com.creative.mvvm.di.component

import com.creative.mvvm.di.ActivityScope
import com.creative.mvvm.di.module.ActivityModule
import com.creative.mvvm.ui.launch.XLauncherActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(activity: XLauncherActivity)
}