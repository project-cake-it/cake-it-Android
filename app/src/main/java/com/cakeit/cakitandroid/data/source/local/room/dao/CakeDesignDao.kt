package com.cakeit.cakitandroid.data.source.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cakeit.cakitandroid.data.source.local.entity.CakeDesignData
import com.cakeit.cakitandroid.data.source.local.entity.CakeShopData

@Dao
interface CakeDesignDao {

    @Query("SELECT * FROM cakedesign")
    fun getCakeDesignList() : LiveData<List<CakeDesignData>>

    @Insert
    fun insertCakeDesign(cakeDesign : CakeDesignData)
}