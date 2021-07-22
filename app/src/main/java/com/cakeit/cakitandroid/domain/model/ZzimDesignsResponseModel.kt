package com.cakeit.cakitandroid.domain.model

import com.cakeit.cakitandroid.di.api.responses.DesignDetailData

class ZzimDesignsResponseModel(
    var message : String,
    var data : ArrayList<DesignDetailData>
) : BaseModel()