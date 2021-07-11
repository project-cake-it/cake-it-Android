package com.cakeit.cakitandroid.di.api.responses

data class PopularCakeResponse(
    var status : Int,
    var message : String,
    var data : ArrayList<DesignListResponseData>
)