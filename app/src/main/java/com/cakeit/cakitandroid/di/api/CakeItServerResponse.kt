package com.cakeit.cakitandroid.di.api

data class CakeItServerResponse<T>(
        var status : Int,
        var message : String,
        var data : T
)