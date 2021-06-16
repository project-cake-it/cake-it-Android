package com.cakeit.cakitandroid.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cakeit.cakitandroid.domain.model.CakeSizeAndrPrice

@Entity(tableName="CakeDesign")
data class CakeDesignData (
     @PrimaryKey(autoGenerate = true)
     var designIndex : Long?,
     var designShopLoc : String,
     var designSize : String,
     var designShopName : String,
     var designPrice : Long,
     var designImagUrl : String?
)