package com.cakeit.cakitandroid.domain.model

import com.cakeit.cakitandroid.di.api.responses.DesignListResponseData

data class DesignListResponseModel (
        var message : String,
        var data : ArrayList<DesignListResponseData>
) : BaseModel()