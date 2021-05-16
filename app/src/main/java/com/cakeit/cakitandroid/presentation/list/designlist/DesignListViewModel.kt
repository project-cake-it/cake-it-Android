package com.cakeit.cakitandroid.presentation.list.designlist

import android.app.Application
import androidx.lifecycle.LiveData
import com.cakeit.cakitandroid.base.BaseViewModel
import com.cakeit.cakitandroid.data.repository.CakeDesignRepo
import com.cakeit.cakitandroid.data.source.local.entity.CakeDesignData
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class DesignListViewModel(application : Application) : BaseViewModel<Any?>(application) {

    private val cakeDesignRepo : CakeDesignRepo
    var cakeDesignItems : LiveData<List<CakeDesignData>>

    init {
        cakeDesignRepo = CakeDesignRepo(application)
        cakeDesignItems = cakeDesignRepo.getCakeDesignList()
    }

    fun insertCakeDesign(cakeDesign : CakeDesignData) {
        Observable.just(cakeDesign)
            .subscribeOn(Schedulers.io())
            .subscribe({
                cakeDesignRepo.insertCakeDesign(cakeDesign)
            }, {
            // Handle error.
        })
    }

    fun getCakeDesignList() : LiveData<List<CakeDesignData>> {
        return cakeDesignItems
    }
}