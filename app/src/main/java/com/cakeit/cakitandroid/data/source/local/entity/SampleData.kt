package com.cakeit.cakitandroid.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SampleData (
    @PrimaryKey(autoGenerate = true)
    var index : Int,
    var keyword : String,
    var content : String
)