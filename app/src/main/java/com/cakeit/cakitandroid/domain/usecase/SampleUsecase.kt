package com.cakeit.cakitandroid.domain.usecase

import com.cakeit.cakitandroid.domain.model.SampleModel


class SampleUsecase : BaseUsecase<List<SampleModel>>(){

    override suspend fun job(vararg args : Any?): List<SampleModel> {
        // Do some data layer jobs
//        val data_as_model = SampleRepo(SampleDao)


        return listOf()
    }
}