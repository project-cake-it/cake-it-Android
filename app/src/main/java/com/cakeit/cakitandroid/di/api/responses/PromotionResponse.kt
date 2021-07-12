package com.cakeit.cakitandroid.di.api.responses

data class PromotionResponse(
    var status : Int,
    var message : String,
    var data : ArrayList<PromotionResponseData>
)

data class PromotionResponseData(
    var id : Int,
    var imageUrl : String,
    var designId : Int
)
