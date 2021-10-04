package com.cakeit.cakitandroid.presentation.list.designlist

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cakeit.cakitandroid.base.BaseViewModel
import com.cakeit.cakitandroid.data.repository.CakeDesignRepo
import com.cakeit.cakitandroid.data.source.local.entity.CakeDesignData
import com.cakeit.cakitandroid.domain.usecase.DesignListUseCase
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class DesignListViewModel(application : Application) : BaseViewModel<Any?>(application) {

    private val cakeDesignRepo : CakeDesignRepo

    private val _cakeDesignItems = MutableLiveData<ArrayList<CakeDesignData>>()
    val cakeDesignItems : LiveData<ArrayList<CakeDesignData>> get() = _cakeDesignItems

    init {
        cakeDesignRepo = CakeDesignRepo(application)
//        cakeDesignItems = cakeDesignRepo.getCakeDesignList()
    }

    fun sendParamsForDesignList(theme : String?, locList : ArrayList<String>, sizeList : ArrayList<String>,
                                colorList : ArrayList<String>, categoryList : ArrayList<String>
                                , order : String?) {

        DesignListUseCase.execute(
                DesignListUseCase.Request(theme, locList, sizeList, colorList, categoryList, order),
                onSuccess = {

                    var cakeDesigns = ArrayList<CakeDesignData>()
                    for(i in 0 .. it.data.size-1 ) {
                        cakeDesigns.add(CakeDesignData(it.data[i].id.toLong(), it.data[i].name, it.data[i].shopAddress, it.data[i].sizes[0].name, it.data[i].shopName, it.data[i].sizes[0].price.toLong(), it.data[i].designImages[0].designImageUrl))

                        // 사이즈 하나만 추가
//                        for(j in 0 .. it.data[i].sizes.size - 1) {
//                            if(it.data[i].designImages.size > 0) cakeDesigns.add(CakeDesignData(it.data[i].id.toLong(), it.data[i].name, it.data[i].shopAddress, it.data[i].sizes[j].name, it.data[i].shopName, it.data[i].sizes[j].price.toLong(), it.data[i].designImages[0].designImageUrl))
//                            else cakeDesigns.add(CakeDesignData(it.data[i].id.toLong(), it.data[i].name, it.data[i].shopAddress, it.data[i].sizes[j].name, it.data[i].shopName, it.data[i].sizes[j].price.toLong(), null))
//                        }
                    }
                    _cakeDesignItems.value = cakeDesigns
                    Log.d("songjem", "DesignList onSuccess")
                },
                onError = {
                    Log.d("songjem", "DesignList onError")
                },
                onFinished = {
                    Log.d("songjem", "DesignList Finished")
                }
        )
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

//    fun getCakeDesignList() : LiveData<List<CakeDesignData>> {
//        return cakeDesignItems
//    }
}