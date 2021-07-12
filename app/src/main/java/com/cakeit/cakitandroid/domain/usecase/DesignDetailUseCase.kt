package com.cakeit.cakitandroid.domain.usecase

import android.util.Log
import com.cakeit.cakitandroid.data.repository.DesignDetailRepo
import com.cakeit.cakitandroid.domain.model.DesignDetailResponseModel
import com.cakeit.cakitandroid.domain.usecase.base.SingleUseCase
import io.reactivex.Single

object DesignDetailUseCase: SingleUseCase<DesignDetailResponseModel>() {

    override fun buildUseCase(baseRequest: BaseRequest): Single<DesignDetailResponseModel>? {
        var req = baseRequest as Request

        return DesignDetailRepo.sendDesignId(req.designId)
            ?.map {
                Log.d("nulkong", "getShopDetail, message = " + it.message)
                DesignDetailResponseModel(it.message, it.data)
            }
    }

    data class Request(
        val designId: Int
    ) : BaseRequest()

    override fun buildUseCase(): Single<DesignDetailResponseModel>? {
        return null
    }
}