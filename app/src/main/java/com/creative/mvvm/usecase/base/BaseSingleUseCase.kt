package com.creative.mvvm.usecase.base

import io.reactivex.Scheduler
import io.reactivex.Single

abstract class BaseSingleUseCase<UseCaseInput, UseCaseOutput> (private val executionThread: Scheduler,
                                                         private val postExecutionThread: Scheduler) : BaseUseCase() {

    protected abstract fun create(input: UseCaseInput): Single<UseCaseOutput>

    fun execute(input: UseCaseInput): Single<UseCaseOutput> {
        return create(input)
            .doOnError(this::doOnError)
            .subscribeOn(executionThread)
            .observeOn(postExecutionThread)
    }
}