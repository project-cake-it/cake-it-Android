package com.cakeit.cakitandroid.di.api.responses

import com.cakeit.cakitandroid.domain.model.CakeSizeAndrPrice

data class ShopListResponse(
        var status : Int,
        var message : String,
        var data : ArrayList<ShopListResponseData>
)

data class ShopListResponseData(
        var shopId : Int,
        var shopAddress : String,
        var shopImage: String,
        var hashTag : ArrayList<String>,
        var prices : ArrayList<CakeSizeAndrPrice>
)