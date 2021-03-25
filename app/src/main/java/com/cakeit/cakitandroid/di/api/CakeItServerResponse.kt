package com.cakeit.cakitandroid.di.api

// data를 string으로 받은 후 error가 아닐경우 변환해야함
data class CakeItServerResponse(
        var status : Int,
        var message : String,
        var data : String
)