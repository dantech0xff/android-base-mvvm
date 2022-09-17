package com.creative.mvvm.di.component

import android.app.Application
import android.content.Context
import com.creative.mvvm.MvvmApp
import com.creative.mvvm.data.AppDatabase
import com.creative.mvvm.data.note.NoteRepo
import com.creative.mvvm.data.app.AppRepo
import com.creative.mvvm.di.ApplicationContext
import com.creative.mvvm.di.CacheDirectory
import com.creative.mvvm.di.FileDirectory
import com.creative.mvvm.di.module.ApplicationModule
import com.creative.mvvm.google.AdmobHelper
import com.creative.mvvm.google.IAPRemoveAdsHelper
import com.creative.mvvm.utils.BaseSchedulerProvider
import dagger.Component
import java.io.File
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(app: MvvmApp)

    fun getApplication(): Application

    @ApplicationContext
    fun getContext(): Context

    @FileDirectory
    fun getFileRootPath(): File

    @CacheDirectory
    fun getCacheRootPath(): File

    fun getIAPRemoveAdsHelper(): IAPRemoveAdsHelper

    fun getAppDatabase(): AppDatabase

    fun getAdmobHelper(): AdmobHelper

    fun getNoteRepo(): NoteRepo

    fun getAppRepo(): AppRepo

    fun getAppScheduler(): BaseSchedulerProvider
}