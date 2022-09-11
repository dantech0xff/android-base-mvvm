package com.creative.mvvm.utils

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Add the disposable to a CompositeDisposable.
 * @param compositeDisposable CompositeDisposable to add this disposable to
 * @return this instance
 */
fun Disposable.addTo(compositeDisposable: CompositeDisposable): Disposable = apply { compositeDisposable.add(this) }