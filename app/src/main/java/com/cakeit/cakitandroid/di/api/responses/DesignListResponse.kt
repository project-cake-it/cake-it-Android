package com.cakeit.cakitandroid.di.api.responses

data class DesignListResponse(
        var status : Int,
        var message : String,
        var data : ArrayList<DesignDetailData>
)

data class DesignListResponseData(
        var id : Int,
        var name : String,
        var themeNames: String,
        var theme : String,
        var category : String,
        var color : String,
        var designImages : ArrayList<DesignImages>,
        var shopId : Int,
        var shopName : String,
        var shopAddress : String,
        var shopFullAddress : String,
        var sizes : ArrayList<DesignSize>,
        var creamNames : String,
        var sheetNames : String,
        var shopChannel : String,
        var zzim : Boolean
)

data class DesignImages (
        var id : Int,
        var designImageUrl : String
)

data class DesignSize (
        var id : Int,
        var name : String,
        var price : String
)