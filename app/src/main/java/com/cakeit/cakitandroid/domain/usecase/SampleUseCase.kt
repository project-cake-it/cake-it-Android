package com.cakeit.cakitandroid.domain.usecase

import com.cakeit.cakitandroid.data.repository.SampleRepo
import com.cakeit.cakitandroid.data.source.local.room.dao.SampleDao
import com.cakeit.cakitandroid.domain.usecase.base.SingleUseCase
import io.reactivex.Single

object SampleUseCase : SingleUseCase<List<SampleRepo>>(){
    override fun buildUseCase(baseRequest: BaseRequest): Single<List<SampleRepo>> {
        var req = baseRequest as Request

        return Single.just(listOf())
    }

    data class Request(
        val sampleDao : SampleDao
    ) : BaseRequest()
}