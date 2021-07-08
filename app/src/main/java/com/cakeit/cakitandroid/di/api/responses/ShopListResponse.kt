package com.cakeit.cakitandroid.di.api.responses

import com.cakeit.cakitandroid.domain.model.CakeShopHashTag
import com.cakeit.cakitandroid.domain.model.CakeSizeAndrPrice

data class ShopListResponse(
        var status : Int,
        var message : String,
        var data : ArrayList<ShopListResponseData>
)

data class ShopListResponseData(
        var id : Int,
        var name : String,
        var address: String,
        var pullAddress : String,
        var information : String,
        var operationTime : String,
        var pickupTime : String,
        var holiday : String,
        var telephone : String,
        var kaokaoMap : String,
        var shopChannel : String,
        var shopImages : ArrayList<CakeShopImage>,
        var themeNames : String,
        var hashtags : ArrayList<CakeShopHashTag>,
        var sizes : ArrayList<CakeSizeAndrPrice>,
        var creamNames : String,
        var sheetNames : String,
        var ziimCount : Int,
        var designs : ArrayList<CakeShopDesign>,
        var zzim : Boolean
)

data class CakeShopImage (
        var id : Int,
        var shopImageUrl : String
)

data class CakeShopDesign (
        var id : Int,
        var name : String,
        var designImage : CakeDesignImage
)

data class CakeDesignImage (
        var id : Int,
        var designImageUrl : String
)