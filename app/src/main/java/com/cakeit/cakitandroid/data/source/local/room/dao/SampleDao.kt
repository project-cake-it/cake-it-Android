package com.cakeit.cakitandroid.data.source.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cakeit.cakitandroid.data.source.local.entity.SampleData

@Dao
interface SampleDao {

    @Query("SELECT * FROM sampledata WHERE keyword = :keyword")
    fun selectSampleDataByKeyword(keyword : String) : SampleData

    @Query("SELECT * FROM sampledata")
    suspend fun selectSampleData() : List<SampleData>

    @Insert
    fun insertSampleData(sampleData: SampleData) : Long

    @Update
    fun updateSampleData(sampleData : SampleData)

    @Delete
    fun deleteSampleData(sampleData: SampleData)
}