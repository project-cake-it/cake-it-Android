package com.cakeit.cakitandroid.presentation.shoplist

import android.app.Application
import androidx.lifecycle.LiveData
import com.cakeit.cakitandroid.base.BaseViewModel
import com.cakeit.cakitandroid.data.repository.CakeShopRepo
import com.cakeit.cakitandroid.data.source.local.entity.CakeShopData

class ShopListViewModel(application : Application) : BaseViewModel<Any?>(application) {

    private val cakeShopRepo : CakeShopRepo
    private val cakeShopItems : LiveData<List<CakeShopData>>

    init {
        cakeShopRepo = CakeShopRepo(application)
        cakeShopItems = cakeShopRepo.getCakeShopList()
    }

    fun insertCakeShop(cakeShop : CakeShopData) {
        cakeShopRepo.insertCakeShop(cakeShop)
    }

    fun getCakeShopList() : LiveData<List<CakeShopData>> {
        return cakeShopItems
    }
}