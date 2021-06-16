package com.cakeit.cakitandroid.data.source.remote.entity

import com.cakeit.cakitandroid.domain.model.CakeSizeAndrPrice

data class CakeShopData (
        var shopId : Int,
        var shopAddress : String,
        var shopImage: String?,
        var hashTag : ArrayList<String>,
        var prices : ArrayList<CakeSizeAndrPrice>
)