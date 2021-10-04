package com.cakeit.cakitandroid.di.api.responses

data class DesignDetailResponse(
    var status : Int,
    var message : String,
    var data : DesignDetailResponseData
)

data class DesignDetailResponseData(
    var design : DesignDetailData
)

data class DesignDetailData(
    var id : Int,
    var name : String,
    var themeNames : String,
    var theme : String,
    var category : String,
    var color : String,
    var designImages : ArrayList<ImageData>,
    var shopId : Int,
    var shopName : String,
    var shopAddress : String,
    var shopFullAddress : String,
    var sizes : ArrayList<SizeData>,
    var creamNames : String,
    var sheetNames : String,
    var zzimCount : Int,
    var shopChannel : String,
    var displaySize : SizeData,
    var zzim : Boolean,
    var displayImage : String,
    var orderAvailabilityDates : ArrayList<String>
)