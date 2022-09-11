package com.creative.mvvm.di.component

import com.creative.mvvm.di.DialogFragmentScope
import com.creative.mvvm.di.module.DialogFragmentModule
import dagger.Component

@DialogFragmentScope
@Component(dependencies = [ApplicationComponent::class],
modules = [DialogFragmentModule::class])
interface DialogFragmentComponent {
}