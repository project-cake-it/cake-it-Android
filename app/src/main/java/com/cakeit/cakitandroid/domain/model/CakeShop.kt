package com.cakeit.cakitandroid.domain.model

data class CakeShop (
    var shopName : String,
    var shopAddress : String,
    var shopTag : ArrayList<String>,
    var shopSize : ArrayList<CakeSizeAndrPrice>
)