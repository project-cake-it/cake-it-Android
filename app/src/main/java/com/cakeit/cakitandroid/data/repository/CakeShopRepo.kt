package com.cakeit.cakitandroid.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.cakeit.cakitandroid.data.source.local.entity.CakeShopData
import com.cakeit.cakitandroid.data.source.local.room.CakeItDatabase
import com.cakeit.cakitandroid.data.source.local.room.dao.CakeShopDao


//TODO("Data layer에서 model로의 변환을 완료한 후 넘겨줘야 합니다.")

class CakeShopRepo(application: Application) {

    private var cakeItDatabase : CakeItDatabase
    private var cakeShopDao : CakeShopDao
    private var cakeShopItems : LiveData<List<CakeShopData>>

    init {
        cakeItDatabase = CakeItDatabase.getDatabase(application)
        cakeShopDao = cakeItDatabase.cakeShopDao()
        cakeShopItems = cakeShopDao.getCakeShopList()
    }

    fun getCakeShopList() : LiveData<List<CakeShopData>> {
        Log.d("songjem", "CakeShopRepo getCakeShopList()")
        return cakeShopItems
    }

    fun insertCakeShop(cakeShop: CakeShopData) {
        Thread(Runnable {
            cakeShopDao.insertCakeShop(cakeShop)
        }).start()
    }
}