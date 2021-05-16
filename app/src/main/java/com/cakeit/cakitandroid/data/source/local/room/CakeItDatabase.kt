package com.cakeit.cakitandroid.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cakeit.cakitandroid.data.source.local.entity.CakeDesignData
import com.cakeit.cakitandroid.data.source.local.entity.CakeShopData
import com.cakeit.cakitandroid.data.source.local.entity.SampleData
import com.cakeit.cakitandroid.data.source.local.room.dao.CakeDesignDao
import com.cakeit.cakitandroid.data.source.local.room.dao.CakeShopDao
import com.cakeit.cakitandroid.data.source.local.room.dao.SampleDao

@Database(entities = arrayOf(SampleData::class, CakeShopData::class, CakeDesignData::class), version = 2, exportSchema = false)
abstract class CakeItDatabase : RoomDatabase() {
    abstract fun sampleDao() : SampleDao
    abstract fun cakeShopDao() : CakeShopDao
    abstract fun cakeDesignDao() : CakeDesignDao

    companion object {
        @Volatile
        private var INSTANCE: CakeItDatabase? = null

        fun getDatabase(context: Context): CakeItDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                        CakeItDatabase::class.java,
                    "cakeit_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}