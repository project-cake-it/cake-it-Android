package com.cakeit.cakitandroid.domain.model

import com.cakeit.cakitandroid.di.api.responses.ShopListResponseData

data class ShopListResponseModel (
        var message : String,
        var data : ArrayList<ShopListResponseData>
) : BaseModel()