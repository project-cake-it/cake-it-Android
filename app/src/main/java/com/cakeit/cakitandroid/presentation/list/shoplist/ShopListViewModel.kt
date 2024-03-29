package com.cakeit.cakitandroid.presentation.list.shoplist

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cakeit.cakitandroid.base.BaseViewModel
import com.cakeit.cakitandroid.data.repository.CakeShopRepo
import com.cakeit.cakitandroid.data.source.remote.entity.CakeShopData
import com.cakeit.cakitandroid.domain.usecase.ShopListUseCase

class ShopListViewModel(application : Application) : BaseViewModel<Any?>(application) {

    private val cakeShopRepo : CakeShopRepo
    private val _cakeShopItems = MutableLiveData<ArrayList<CakeShopData>>()
    val cakeShopItems : LiveData<ArrayList<CakeShopData>> get() = _cakeShopItems

    init {
        cakeShopRepo = CakeShopRepo(application)
//        cakeShopItems = cakeShopRepo.getCakeShopList()
    }

    fun sendParamsForShopList(order : String?, locList : ArrayList<String>, pickup : String?) {

        ShopListUseCase.execute(
                ShopListUseCase.Request(order, locList, pickup),
                onSuccess = {

                    var cakeShops = ArrayList<CakeShopData>()

                    for(i in 0 .. it.data.size-1 ) {
                        cakeShops.add(CakeShopData(it.data[i].id, it.data[i].name, it.data[i].address, it.data[i].shopImages, it.data[i].hashtags!!, it.data[i].sizes!!))
                    }
                    _cakeShopItems.value = cakeShops
                    Log.d("songjem", "ShopList onSuccess")
                },
                onError = {
                    Log.d("songjem", "ShopList onError")
                },
                onFinished = {
                    Log.d("songjem", "ShopList Finished")
                }
        )
    }

//    fun insertCakeShop(cakeShop : CakeShopData) {
//        Observable.just(cakeShop)
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                cakeShopRepo.insertCakeShop(cakeShop)
//            }, {
//            // Handle error.
//        })
//    }

//    fun getCakeShopList() : LiveData<List<CakeShopData>> {
//        return cakeShopItems
//    }
}