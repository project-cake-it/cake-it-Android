package com.cakeit.cakitandroid.di.api.responses

data class ImageData(
    var  id : Int,
    var designImageUrl : String
)

data class DesignData(
    var id : Int,
    var name : String,
    var designImage : ImageData
)

data class SizeData(
    var id : Int,
    var name : String,
    var size : String,
    var price : Int
)

data class HashtagData(
    var id : Int,
    var name : String
)

data class ShopDetailData(
    var id : Int,
    var name : String,
    var address : String,
    var pullAddress : String,
    var information : String,
    var operationTime : String,
    var pickupTime : String,
    var holiday : String,
    var telephone : String,
    var kakaoMap : String,
    var shopChannel : String,
    var themeNames : String,
    var hashtags :  ArrayList<HashtagData>,
    var sizes : ArrayList<SizeData>,
    var creamNames : String,
    var sheetNames : String,
    var zzimCount : Int,
    var designs : ArrayList<DesignData>,
    var zzim : Boolean
)

data class ShopDetailResponseData(
    var shop : ShopDetailData
)

data class ShopDetailResponse(
    var status : Int,
    var message : String,
    var data : ShopDetailResponseData
)