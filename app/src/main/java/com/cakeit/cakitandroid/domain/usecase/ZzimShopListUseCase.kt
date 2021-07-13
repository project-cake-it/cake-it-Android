package com.cakeit.cakitandroid.domain.usecase

import android.util.Log
import com.cakeit.cakitandroid.data.repository.ZzimShopListRepo
import com.cakeit.cakitandroid.domain.model.ZzimShopResponseModel
import com.cakeit.cakitandroid.domain.usecase.base.SingleUseCase
import io.reactivex.Single

object ZzimShopListUseCase : SingleUseCase<ZzimShopResponseModel>(){
    override fun buildUseCase(baseRequest: BaseRequest): Single<ZzimShopResponseModel>? {
        var req = baseRequest as ZzimShopListUseCase.Request
        Log.d("ssongjem", "buildUseCase")
        return ZzimShopListRepo.sendParams()
            ?.map {
                Log.d("songjem", "getZzimShopList, message = " + it.message)
                ZzimShopResponseModel(it.message, it.data)
            }
    }

    data class Request(
        val authCode : String
    ) : BaseRequest()

    override fun buildUseCase(): Single<ZzimShopResponseModel>? {
        return null
    }
}