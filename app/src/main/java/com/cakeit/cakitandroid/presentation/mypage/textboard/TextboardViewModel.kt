package com.cakeit.cakitandroid.presentation.mypage.textboard

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cakeit.cakitandroid.base.BaseViewModel
import com.cakeit.cakitandroid.domain.model.TextboardModel

class TextboardViewModel(application: Application) : BaseViewModel<Any?>(application)  {

    private val _textboardItem = MutableLiveData<TextboardModel>()
    val textboardItem : LiveData<TextboardModel> get() = _textboardItem

    fun setTextboardItem(textboardModel : TextboardModel){
        _textboardItem.value = textboardModel
    }
}