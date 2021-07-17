package com.cakeit.cakitandroid.domain.usecase

import android.util.Log
import com.cakeit.cakitandroid.data.repository.ZzimDesignRegRepo
import com.cakeit.cakitandroid.domain.model.ZzimResponseModel
import com.cakeit.cakitandroid.domain.usecase.base.SingleUseCase
import io.reactivex.Single

object ZzimDesignRegUseCase : SingleUseCase<ZzimResponseModel>(){
    override fun buildUseCase(baseRequest: BaseRequest): Single<ZzimResponseModel>? {
        var req = baseRequest as Request

        return ZzimDesignRegRepo.sendDesignId(req.designId)
            ?.map {
                Log.d("songjem", "postZziDesignRepo, message = " + it.message)
                ZzimResponseModel(it.message, it.data)
            }
    }

    data class Request(
        val designId : Int
    ) : BaseRequest()

    override fun buildUseCase(): Single<ZzimResponseModel>? {
        return null
    }
}