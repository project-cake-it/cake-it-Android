package com.cakeit.cakitandroid.domain.usecase.base

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class BaseUseCase<T> : UseCase() {

    internal abstract fun buildUseCase(): Single<T>

    fun execute(
        onSuccess: ((t: T) -> Unit),
        onError: ((t: Throwable) -> Unit),
        onFinished: () -> Unit = {}
    ) {
        disposeLast()
        lastDisposable = buildUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate(onFinished)
            .subscribe(onSuccess, onError)

        lastDisposable?.let {
            compositeDisposable.add(it)
        }
    }
}