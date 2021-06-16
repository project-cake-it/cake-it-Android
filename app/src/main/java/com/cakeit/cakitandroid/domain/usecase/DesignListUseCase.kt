package com.cakeit.cakitandroid.domain.usecase

import android.util.Log
import com.cakeit.cakitandroid.data.repository.DesignListRepo
import com.cakeit.cakitandroid.data.repository.SocialLoginRepo
import com.cakeit.cakitandroid.domain.model.DesignListResponseModel
import com.cakeit.cakitandroid.domain.model.SocialLoginResponseModel
import com.cakeit.cakitandroid.domain.usecase.base.SingleUseCase
import io.reactivex.Single

object DesignListUseCase : SingleUseCase<DesignListResponseModel>(){
    override fun buildUseCase(baseRequest: BaseRequest): Single<DesignListResponseModel>? {
        var req = baseRequest as Request

        return DesignListRepo.sendParams(req.theme, req.locList, req.sizeList, req.colorList, req.categoryList, req.order)
                ?.map {
                    Log.d("songjem", "getDesignList, message = " + it.message)
                    DesignListResponseModel(it.message, it.data)
                }
    }

    data class Request(
            val theme : String?,
            val locList : ArrayList<String>,
            var sizeList : ArrayList<String>,
            var colorList : ArrayList<String>,
            var categoryList : ArrayList<String>,
            var order : String
    ) : BaseRequest()
}