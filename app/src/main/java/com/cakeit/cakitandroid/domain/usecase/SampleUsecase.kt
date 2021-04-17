package com.cakeit.cakitandroid.domain.usecase

import com.cakeit.cakitandroid.data.repository.SampleRepo
import com.cakeit.cakitandroid.data.source.local.room.dao.SampleDao
import com.cakeit.cakitandroid.domain.usecase.base.BaseUseCase
import io.reactivex.Single

class SampleUseCase : BaseUseCase<List<SampleRepo>>(){
    override fun buildUseCase(): Single<List<SampleRepo>> {
        return Single.just(listOf<SampleRepo>())
    }
}