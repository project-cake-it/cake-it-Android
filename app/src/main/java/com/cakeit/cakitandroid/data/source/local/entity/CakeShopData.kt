package com.cakeit.cakitandroid.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="CakeShop")
data class CakeShopData (
     @PrimaryKey(autoGenerate = true)
     var shopIndex : Long?,
     var shopName : String,
     var shopAddress : String
)