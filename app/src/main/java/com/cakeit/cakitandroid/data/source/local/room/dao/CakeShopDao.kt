package com.cakeit.cakitandroid.data.source.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cakeit.cakitandroid.data.source.local.entity.CakeShopData

@Dao
interface CakeShopDao {

    @Query("SELECT * FROM cakeshop")
    fun getCakeShopList() : LiveData<List<CakeShopData>>

    @Insert
    fun insertCakeShop(cakeShop : CakeShopData)
}