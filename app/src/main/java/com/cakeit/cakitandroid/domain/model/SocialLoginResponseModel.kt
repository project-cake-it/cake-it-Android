package com.cakeit.cakitandroid.domain.model

data class SocialLoginResponseModel (
        var message : String,
        var accessToken : String
) : BaseModel()