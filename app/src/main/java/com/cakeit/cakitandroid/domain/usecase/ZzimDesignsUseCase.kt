package com.cakeit.cakitandroid.domain.usecase

import android.util.Log
import com.cakeit.cakitandroid.data.repository.PromotionRepo
import com.cakeit.cakitandroid.data.repository.ZzimDesignsRepo
import com.cakeit.cakitandroid.domain.model.PromotionResponseModel
import com.cakeit.cakitandroid.domain.model.ZzimDesignsResponseModel
import com.cakeit.cakitandroid.domain.usecase.base.SingleUseCase
import io.reactivex.Single

object ZzimDesignsUseCase : SingleUseCase<ZzimDesignsResponseModel>(){

    override fun buildUseCase(baseRequest: BaseRequest): Single<ZzimDesignsResponseModel>? {
        var req = baseRequest as Request

        return ZzimDesignsRepo.getDesigns(req.authorization)
            ?.map {
                Log.d("nulkong", "getDesigns, message = " + it.message)
                ZzimDesignsResponseModel(it.message, it.data)
            }
    }

    data class Request(
        val authorization : String
    ) : BaseRequest()

    override fun buildUseCase(): Single<ZzimDesignsResponseModel>? {
        return null
    }
}