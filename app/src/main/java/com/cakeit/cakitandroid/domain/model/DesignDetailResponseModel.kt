package com.cakeit.cakitandroid.domain.model

import com.cakeit.cakitandroid.di.api.responses.DesignDetailResponseData

class DesignDetailResponseModel (
    var message : String,
    var data : DesignDetailResponseData
) : BaseModel()