package com.cakeit.cakitandroid.presentation.mypage.announcement

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cakeit.cakitandroid.base.BaseViewModel
import com.cakeit.cakitandroid.domain.model.TextboardModel
import com.cakeit.cakitandroid.domain.usecase.AnnouncementListUseCase
import com.cakeit.cakitandroid.domain.usecase.BaseRequest

class AnnouncementListViewModel(application: Application) : BaseViewModel<Any?>(application)  {

    private val _announcementListItems = MutableLiveData<ArrayList<TextboardModel>>()
    val announcementListItems : LiveData<ArrayList<TextboardModel>> get() = _announcementListItems

    init {

    }
    
    // For local test. Including long titles and bodies
//    fun add(){
//        val tms = arrayListOf<TextboardModel>()
//        tms.add(TextboardModel(1, "test1", "testBody1", "2020.11.14"))
//        tms.add(TextboardModel(2, "test2", "testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1testBody1", "2020.11.14"))
//        tms.add(TextboardModel(3, "test3test3test3test3test3test3test3test3test3test3test3", "testBody1", "2020.11.14"))
//        tms.add(TextboardModel(4, "final", "testBody1", "2020.11.14"))
//        _announcementListItems.value = tms
//    }

    fun getAnnouncementListFromServer(){
        Log.d("sungmin", "getAnnouncementListFromServer Start")
        AnnouncementListUseCase.execute(
            onSuccess = {
                it.map{ textboardModel ->
                    textboardModel.date = textboardModel.date?.substring(0..9)
                }
                _announcementListItems.value = it
            }, onFinished = {
                Log.d("sungmin", "getAnnouncementListFromServer Finished")
            }, onError = {
                Log.d("sungmin", "getAnnouncementListFromServer Error")
            }
        )
    }
}