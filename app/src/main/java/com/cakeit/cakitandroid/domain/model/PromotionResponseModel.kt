package com.cakeit.cakitandroid.domain.model

import com.cakeit.cakitandroid.di.api.responses.PromotionResponseData

class PromotionResponseModel(
    var message : String,
    var data : ArrayList<PromotionResponseData>
) : BaseModel()