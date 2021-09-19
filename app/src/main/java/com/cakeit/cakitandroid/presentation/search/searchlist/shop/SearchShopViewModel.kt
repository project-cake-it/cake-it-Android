package com.cakeit.cakitandroid.presentation.search.searchlist.shop

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cakeit.cakitandroid.base.BaseViewModel
import com.cakeit.cakitandroid.data.source.remote.entity.CakeShopData
import com.cakeit.cakitandroid.domain.usecase.SearchShopUseCase

class SearchShopViewModel(application: Application) : BaseViewModel<Any?>(application) {

    private val _cakeShopItems = MutableLiveData<ArrayList<CakeShopData>>()
    val cakeShopItems : LiveData<ArrayList<CakeShopData>> get() = _cakeShopItems

    fun sendParamsForSearchShop(keyword : String?, name : String?, theme : String?, locList : ArrayList<String>?, sizeList : ArrayList<String>?,
                                colorList : ArrayList<String>?, categoryList : ArrayList<String>?
                                , order : String) {

        Log.d("songjem", "searchShop, keyword = " + keyword + ", name = " + name)
        SearchShopUseCase.execute(
            SearchShopUseCase.Request(keyword!!, name, locList!!, theme, sizeList!!, colorList!!, categoryList!!, order),
            onSuccess = {

                var cakeShops = ArrayList<CakeShopData>()
                for(i in 0 .. it.data.shops.size-1 ) {
                    cakeShops.add(CakeShopData(it.data.shops[i].id, it.data.shops[i].name, it.data.shops[i].address, it.data.shops[i].shopImages, it.data.shops[i].hashtags!!, it.data.shops[i].sizes!!))
                }
                _cakeShopItems.value = cakeShops
                Log.d("songjem", "SearchShop onSuccess")
            },
            onError = {
                Log.d("songjem", "SearchShop onError")
            },
            onFinished = {
                Log.d("songjem", "SearchShop Finished")
            }
        )
    }
}