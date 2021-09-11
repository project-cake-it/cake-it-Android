package com.cakeit.cakitandroid.domain.usecase

import android.util.Log
import com.cakeit.cakitandroid.data.repository.MyPageRepo
import com.cakeit.cakitandroid.domain.model.TextboardModel
import com.cakeit.cakitandroid.domain.usecase.base.SingleUseCase
import io.reactivex.Single

object AnnouncementListUseCase  : SingleUseCase<ArrayList<TextboardModel>>(){
    override fun buildUseCase(baseRequest: BaseRequest): Single<ArrayList<TextboardModel>>? {
        return null
    }


    override fun buildUseCase(): Single<ArrayList<TextboardModel>>? {
        Log.d("sungmin", "AnnouncementListUseCase Start")
        return MyPageRepo.getAnnouncementList()
            ?.map {
                ArrayList(it.data.map{

                    TextboardModel(it.id, it.title, it.body, it.createdAt.replace("-", "."))
                })
            }
    }
}