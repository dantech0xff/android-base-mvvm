package com.creative.mvvm.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationContext

@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class ActivityContext

@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class CacheDirectory

@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class FileDirectory