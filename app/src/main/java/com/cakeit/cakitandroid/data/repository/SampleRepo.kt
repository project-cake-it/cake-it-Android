package com.cakeit.cakitandroid.data.repository

import androidx.lifecycle.LiveData
import com.cakeit.cakitandroid.data.source.local.entity.SampleData
import com.cakeit.cakitandroid.data.source.local.room.dao.SampleDao


//TODO("Data layer에서 model로의 변환을 완료한 후 넘겨줘야 합니다.")

class SampleRepo(val sampleDao : SampleDao) {
    fun selectSampleDataByIndex(keyword : String) : SampleData {
        return sampleDao.selectSampleDataByKeyword(keyword)
    }

    suspend fun selectSampleData() : List<SampleData> {
        return sampleDao.selectSampleData()
    }

    fun insertSampleData(sampleData: SampleData) : Long {
        return sampleDao.insertSampleData(sampleData)
    }

    fun updateSampleData(sampleData : SampleData) {
        return sampleDao.updateSampleData(sampleData)
    }

    fun deleteSampleData(sampleData: SampleData) {
        return sampleDao.deleteSampleData(sampleData)
    }
}