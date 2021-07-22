package com.cakeit.cakitandroid.domain.usecase

import android.util.Log
import com.cakeit.cakitandroid.data.repository.ZzimShopDelRepo
import com.cakeit.cakitandroid.data.repository.ZzimShopRegRepo
import com.cakeit.cakitandroid.domain.model.ZzimResponseModel
import com.cakeit.cakitandroid.domain.usecase.base.SingleUseCase
import io.reactivex.Single

object ZzimShopDelUseCase : SingleUseCase<ZzimResponseModel>(){
    override fun buildUseCase(baseRequest: BaseRequest): Single<ZzimResponseModel>? {
        var req = baseRequest as Request

        return ZzimShopDelRepo.sendShopId(req.shopId)
            ?.map {
                Log.d("songjem", "deleteZzimShopRepo, message = " + it.message)
                ZzimResponseModel(it.message, it.data)
            }
    }

    data class Request(
        val shopId : Int
    ) : BaseRequest()

    override fun buildUseCase(): Single<ZzimResponseModel>? {
        return null
    }
}