package com.cakeit.cakitandroid.di.api.responses

data class SocialLoginResponseData(
    var accessToken : String
)

data class SocialLoginResponse(
    var status : Int,
    var message : String,
    var data : SocialLoginResponseData
)