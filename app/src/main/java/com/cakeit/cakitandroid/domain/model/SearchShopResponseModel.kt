package com.cakeit.cakitandroid.domain.model

import com.cakeit.cakitandroid.di.api.responses.KeywordSearchDatas

data class SearchShopResponseModel (
    var message : String,
    var data : KeywordSearchDatas
) : BaseModel()