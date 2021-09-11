package com.cakeit.cakitandroid.domain.usecase

import android.util.Log
import com.cakeit.cakitandroid.data.repository.PopularCakeRepo
import com.cakeit.cakitandroid.domain.model.PopularCakeResponseModel
import com.cakeit.cakitandroid.domain.usecase.base.SingleUseCase
import io.reactivex.Single

object PopularCakeUseCase: SingleUseCase<PopularCakeResponseModel>() {

    override fun buildUseCase(baseRequest: BaseRequest): Single<PopularCakeResponseModel>? {
        var req = baseRequest as PopularCakeUseCase.Request

        return PopularCakeRepo.getPopularCake(req.theme)
            ?.map {
                Log.d("nulkong", "getPromotions, message = " + it.message)
                PopularCakeResponseModel(it.message, it.data)
            }
    }

    override fun buildUseCase(): Single<PopularCakeResponseModel>? {
        return null
    }

    data class Request(
        val theme : String
    ) : BaseRequest()
}