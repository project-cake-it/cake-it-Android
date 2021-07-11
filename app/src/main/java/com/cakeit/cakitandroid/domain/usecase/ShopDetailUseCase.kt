package com.cakeit.cakitandroid.domain.usecase

import android.util.Log
import com.cakeit.cakitandroid.data.repository.ShopDetailRepo
import com.cakeit.cakitandroid.domain.model.ShopDetailResponseModel
import com.cakeit.cakitandroid.domain.usecase.base.SingleUseCase
import io.reactivex.Single


object ShopDetailUseCase : SingleUseCase<ShopDetailResponseModel>(){

    override fun buildUseCase(baseRequest: BaseRequest): Single<ShopDetailResponseModel>? {
        var req = baseRequest as Request

        return ShopDetailRepo.sendShopId(req.shopId)
            ?.map {
                Log.d("nulkong", "getShopDetail, message = " + it.message)
                ShopDetailResponseModel(it.message, it.data)
            }
    }

    data class Request(
        val shopId : Int
    ) : BaseRequest()

    override fun buildUseCase(): Single<ShopDetailResponseModel>? {
        return null
    }
}
