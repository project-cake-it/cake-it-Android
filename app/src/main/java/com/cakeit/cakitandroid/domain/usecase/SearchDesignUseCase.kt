package com.cakeit.cakitandroid.domain.usecase

import android.util.Log
import com.cakeit.cakitandroid.data.repository.SearchDesignRepo
import com.cakeit.cakitandroid.domain.model.SearchDesignResponseModel
import com.cakeit.cakitandroid.domain.usecase.base.SingleUseCase
import io.reactivex.Single

object SearchDesignUseCase : SingleUseCase<SearchDesignResponseModel>(){
    override fun buildUseCase(baseRequest: BaseRequest): Single<SearchDesignResponseModel>? {
        var req = baseRequest as Request

        return SearchDesignRepo.sendParams(req.keyword!!, req.name!!, req.locList, req.theme, req.sizeList, req.colorList, req.categoryList, req.order)
            ?.map {
                Log.d("songjem", "getDesignList, message = " + it.message)
                SearchDesignResponseModel(it.message, it.data)
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
        var order : String
    ) : BaseRequest()
}