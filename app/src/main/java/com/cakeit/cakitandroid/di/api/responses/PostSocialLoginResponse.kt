package com.cakeit.cakitandroid.di.api.responses

import com.cakeit.cakitandroid.data.source.remote.entity.SocialLoginResponseEntity

data class PostSocialLoginResponse(
        var status : Int,
        var message : String,
        var data : SocialLoginResponseEntity
)