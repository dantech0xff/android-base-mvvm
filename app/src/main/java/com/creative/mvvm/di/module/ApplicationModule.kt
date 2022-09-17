package com.creative.mvvm.di.module

import android.app.Application
import android.content.Context
import com.creative.mvvm.MvvmApp
import com.creative.mvvm.data.AppDatabase
import com.creative.mvvm.data.app.AppRepoImpl
import com.creative.mvvm.data.app.AppRepo
import com.creative.mvvm.di.ApplicationContext
import com.creative.mvvm.di.CacheDirectory
import com.creative.mvvm.di.FileDirectory
import com.creative.mvvm.utils.BaseSchedulerProvider
import com.creative.mvvm.utils.AppSchedulerProviderImpl
import dagger.Module
import dagger.Provides
import java.io.File
import javax.inject.Singleton

@Module
class ApplicationModule (private val application: MvvmApp) {

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    @ApplicationContext
    fun provideAppContext(): Context = application

    @Provides
    @Singleton
    @FileDirectory
    fun provideRootFilePath(): File {
        val fileRoot = File(application.filesDir.absolutePath + "/Notes/")
        if (!fileRoot.exists()) {
            fileRoot.mkdir()
        }
        return fileRoot
    }

    @Provides
    @Singleton
    @CacheDirectory
    fun provideRootCachePath(): File {
        val fileRootCache = File(application.cacheDir.absolutePath + "/Notes/")
        if (!fileRootCache.exists()) {
            fileRootCache.mkdir()
        }
        return fileRootCache
    }

    @Provides
    @Singleton
    fun provideAppDatabase(): AppDatabase = AppDatabase.invoke(application)

    @Provides
    @Singleton
    fun provideAppRepo(appRepoImpl: AppRepoImpl): AppRepo = appRepoImpl

    @Provides
    @Singleton
    fun provideAppSchedulerProvider(appSchedulerProviderImpl: AppSchedulerProviderImpl): BaseSchedulerProvider = appSchedulerProviderImpl
}