package com.cakeit.cakitandroid.domain.usecase

import android.util.Log
import com.cakeit.cakitandroid.data.repository.SearchShopRepo
import com.cakeit.cakitandroid.domain.model.SearchShopResponseModel
import com.cakeit.cakitandroid.domain.usecase.base.SingleUseCase
import io.reactivex.Single

object SearchShopUseCase : SingleUseCase<SearchShopResponseModel>(){
    override fun buildUseCase(baseRequest: BaseRequest): Single<SearchShopResponseModel>? {
        var req = baseRequest as Request

        return SearchShopRepo.sendParams(req.keyword, req.name, req.locList, req.theme, req.sizeList, req.colorList, req.categoryList, req.order, req.pickup)
            ?.map {
                Log.d("songjem", "getSearchShop, message = " + it.message)
                SearchShopResponseModel(it.message, it.data)
            }
    }

    data class Request(
        val keyword : String?,
        val name : String?,
        val locList : ArrayList<String>,
        val theme : String?,
        var sizeList : ArrayList<String>,
        var colorList : ArrayList<String>,
        var categoryList : ArrayList<String>,
        var order : String?,
        var pickup : String?
    ) : BaseRequest()

    override fun buildUseCase(): Single<SearchShopResponseModel>? {
        return null
    }
}