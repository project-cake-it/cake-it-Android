package com.cakeit.cakitandroid.presentation.shoplist

import android.app.Application
import androidx.lifecycle.LiveData
import com.cakeit.cakitandroid.base.BaseViewModel
import com.cakeit.cakitandroid.data.repository.CakeShopRepo
import com.cakeit.cakitandroid.data.source.local.entity.CakeShopData
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class ShopListViewModel(application : Application) : BaseViewModel<Any?>(application) {

    private val cakeShopRepo : CakeShopRepo
    var cakeShopItems : LiveData<List<CakeShopData>>

    init {
        cakeShopRepo = CakeShopRepo(application)
        cakeShopItems = cakeShopRepo.getCakeShopList()
    }

    fun insertCakeShop(cakeShop : CakeShopData) {
        Observable.just(cakeShop)
            .subscribeOn(Schedulers.io())
            .subscribe({
                cakeShopRepo.insertCakeShop(cakeShop)
            }, {
            // Handle error.
        })
    }

    fun getCakeShopList() : LiveData<List<CakeShopData>> {
        return cakeShopItems
    }
}