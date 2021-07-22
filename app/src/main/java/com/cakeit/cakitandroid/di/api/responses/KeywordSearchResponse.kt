package com.cakeit.cakitandroid.di.api.responses

data class KeywordSearchResponse (
    var status : Int,
    var message : String,
    var data : KeywordSearchDatas
)

data class KeywordSearchDatas (
    var shops : ArrayList<ShopListResponseData>,
    var designs : ArrayList<DesignListResponseData>
)