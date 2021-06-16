package com.cakeit.cakitandroid.domain.usecase

import android.util.Log
import com.cakeit.cakitandroid.data.repository.ShopListRepo
import com.cakeit.cakitandroid.domain.model.ShopListResponseModel
import com.cakeit.cakitandroid.domain.usecase.base.SingleUseCase
import io.reactivex.Single

object ShopListUseCase : SingleUseCase<ShopListResponseModel>(){
    override fun buildUseCase(baseRequest: BaseRequest): Single<ShopListResponseModel>? {
        var req = baseRequest as Request

        return ShopListRepo.sendParams(req.order, req.locList)
                ?.map {
                    Log.d("songjem", "getShopList, message = " + it.message)
                    ShopListResponseModel(it.message, it.data)
                }
    }

    data class Request(
            val order : String?,
            val locList : ArrayList<String>
    ) : BaseRequest()
}