package com.creative.mvvm.di.module

import com.creative.mvvm.ui.base.BaseDialogFragment
import dagger.Module

@Module
class DialogFragmentModule(private val dialogFragment: BaseDialogFragment<*,*>)