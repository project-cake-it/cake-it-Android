package com.cakeit.cakitandroid.domain.usecase

import android.util.Log
import com.cakeit.cakitandroid.data.repository.ZzimDesignDelRepo
import com.cakeit.cakitandroid.data.repository.ZzimDesignRegRepo
import com.cakeit.cakitandroid.domain.model.ZzimResponseModel
import com.cakeit.cakitandroid.domain.usecase.base.SingleUseCase
import io.reactivex.Single

object ZzimDesignDelUseCase : SingleUseCase<ZzimResponseModel>(){
    override fun buildUseCase(baseRequest: BaseRequest): Single<ZzimResponseModel>? {
        var req = baseRequest as Request

        return ZzimDesignDelRepo.sendDesignId(req.authorization, req.designId)
            ?.map {
                Log.d("songjem", "deleteZziDesignRepo, message = " + it.message)
                ZzimResponseModel(it.message, it.data)
            }
    }

    data class Request(
        val authorization : String,
        val designId : Int
    ) : BaseRequest()

    override fun buildUseCase(): Single<ZzimResponseModel>? {
        return null
    }
}