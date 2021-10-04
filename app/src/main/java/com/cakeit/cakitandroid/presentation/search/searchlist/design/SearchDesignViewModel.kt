package com.cakeit.cakitandroid.presentation.search.searchlist.design

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cakeit.cakitandroid.base.BaseViewModel
import com.cakeit.cakitandroid.data.source.local.entity.CakeDesignData
import com.cakeit.cakitandroid.domain.usecase.SearchDesignUseCase

class SearchDesignViewModel(application: Application) : BaseViewModel<Any?>(application) {

    private val _cakeDesignItems = MutableLiveData<ArrayList<CakeDesignData>>()
    val cakeDesignItems : LiveData<ArrayList<CakeDesignData>> get() = _cakeDesignItems

    fun sendParamsForSearchDesign(keyword : String?, name : String?, theme : String?, locList : ArrayList<String>?, sizeList : ArrayList<String>?,
                                colorList : ArrayList<String>?, categoryList : ArrayList<String>?
                                , order : String) {
        SearchDesignUseCase.execute(
            SearchDesignUseCase.Request(keyword!!, name, locList!!, theme, sizeList!!, colorList!!, categoryList!!, order),
            onSuccess = {

                var cakeDesigns = ArrayList<CakeDesignData>()
                for(i in 0 .. it.data.designs.size-1 ) {
                    cakeDesigns.add(CakeDesignData(it.data.designs[i].id.toLong(), it.data.designs[i].name, it.data.designs[i].shopAddress, it.data.designs[i].sizes[0].name, it.data.designs[i].shopName, it.data.designs[i].sizes[0].price.toLong(), it.data.designs[i].designImages[0].designImageUrl))
                    // 사이즈 하나만 추가
//                    for(j in 0 .. it.data.designs[i].sizes.size - 1) {
//                        if(it.data.designs[i].designImages.size > 0) cakeDesigns.add(CakeDesignData(it.data.designs[i].id.toLong(), it.data.designs[i].name, it.data.designs[i].shopAddress, it.data.designs[i].sizes[j].name, it.data.designs[i].shopName, it.data.designs[i].sizes[j].price.toLong(), it.data.designs[i].designImages[0].designImageUrl))
//                        else cakeDesigns.add(CakeDesignData(it.data.designs[i].id.toLong(), it.data.designs[i].name, it.data.designs[i].shopAddress, it.data.designs[i].sizes[j].name, it.data.designs[i].shopName, it.data.designs[i].sizes[j].price.toLong(), null))
//                    }
                }
                _cakeDesignItems.value = cakeDesigns
                Log.d("songjem", "SearchDesign onSuccess")
            },
            onError = {
                Log.d("songjem", "SearchDesign onError")
            },
            onFinished = {
                Log.d("songjem", "SearchDesign Finished")
            }
        )
    }
}