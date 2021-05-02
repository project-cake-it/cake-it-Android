package com.cakeit.cakitandroid.domain.usecase.base

import com.cakeit.cakitandroid.domain.usecase.BaseRequest
import com.cakeit.cakitandroid.domain.usecase.BaseUseCase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


abstract class SingleBaseUseCase<P> : BaseUseCase() {

    internal abstract fun buildUseCase(request: BaseRequest): Single<P>?

    fun execute(
        request: BaseRequest,
        onSuccess: ((t: P) -> Unit),
        onError: ((t: Throwable) -> Unit),
        onFinished: () -> Unit = {}
    ) {
        disposeLast()
        lastDisposable = buildUseCase(request)
            ?.doAfterTerminate(onFinished)
            ?.subscribe(onSuccess, onError)

        lastDisposable?.let {
            compositeDisposable.add(it)
        }
    }
}