package com.cakeit.cakitandroid.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.cakeit.cakitandroid.data.source.local.entity.CakeDesignData
import com.cakeit.cakitandroid.data.source.local.room.CakeItDatabase
import com.cakeit.cakitandroid.data.source.local.room.dao.CakeDesignDao

//TODO("Data layer에서 model로의 변환을 완료한 후 넘겨줘야 합니다.")

class CakeDesignRepo(application: Application) {

    private var cakeItDatabase : CakeItDatabase
    private var cakeDesignDao : CakeDesignDao
    private var cakeDesignItems : LiveData<List<CakeDesignData>>

    init {
        cakeItDatabase = CakeItDatabase.getDatabase(application)
        cakeDesignDao = cakeItDatabase.cakeDesignDao()
        cakeDesignItems = cakeDesignDao.getCakeDesignList()
    }

    fun getCakeDesignList() : LiveData<List<CakeDesignData>> {
        Log.d("songjem", "CakeDesignRepo getCakeDesignList()")
        return cakeDesignItems
    }

    fun insertCakeDesign(cakeDesign: CakeDesignData) {
        Thread(Runnable {
            cakeDesignDao.insertCakeDesign(cakeDesign)
        }).start()
    }
}