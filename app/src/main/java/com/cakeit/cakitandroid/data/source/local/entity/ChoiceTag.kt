package com.cakeit.cakitandroid.data.source.local.entity

data class ChoiceTag (
     var filterCode : Int,  // 기본순 : 0, 지역 : 1, 크기 : 2, 색깔 : 3, 카테고리 : 4
     var choiceCode : Int,
     var choiceName : String
)