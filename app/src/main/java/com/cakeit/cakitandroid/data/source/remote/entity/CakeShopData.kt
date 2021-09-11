package com.cakeit.cakitandroid.data.source.remote.entity

import com.cakeit.cakitandroid.di.api.responses.CakeShopImage
import com.cakeit.cakitandroid.domain.model.CakeShopHashTag
import com.cakeit.cakitandroid.domain.model.CakeSizeAndrPrice

data class CakeShopData (
        var shopId : Int,
        var shopName : String,
        var shopAddress : String,
        var shopImages : ArrayList<CakeShopImage>,
        var hashTag : ArrayList<CakeShopHashTag>?,
        var prices : ArrayList<CakeSizeAndrPrice>?
)