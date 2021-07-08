package com.cakeit.cakitandroid.domain.model

import com.cakeit.cakitandroid.di.api.responses.ShopDetailResponseData

data class ShopDetailResponseModel(
    var message : String,
    var data : ShopDetailResponseData
) : BaseModel()