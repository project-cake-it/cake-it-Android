package com.cakeit.cakitandroid.domain.usecase

import android.util.Log
import com.cakeit.cakitandroid.data.repository.DesignDetailRepo
import com.cakeit.cakitandroid.data.repository.PromotionRepo
import com.cakeit.cakitandroid.domain.model.DesignDetailResponseModel
import com.cakeit.cakitandroid.domain.model.PromotionResponseModel
import com.cakeit.cakitandroid.domain.usecase.base.SingleUseCase
import io.reactivex.Single

object PromotionUseCase: SingleUseCase<PromotionResponseModel>() {

    override fun buildUseCase(baseRequest: BaseRequest): Single<PromotionResponseModel>? {

        return PromotionRepo.getPromotion()
            ?.map {
                Log.d("nulkong", "getPromotions, message = " + it.message)
                PromotionResponseModel(it.message, it.data)
            }
    }
}