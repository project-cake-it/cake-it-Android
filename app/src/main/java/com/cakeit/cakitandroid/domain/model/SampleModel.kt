package com.cakeit.cakitandroid.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SampleModel")
data class SampleModel(
    @PrimaryKey var id: Long,
    var title: String,
    val url: String,
    val thumbnailUrl: String?){

    fun setName(text: String) {
        title = text
    }
}