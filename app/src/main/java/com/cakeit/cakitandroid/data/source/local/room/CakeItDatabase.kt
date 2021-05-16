package com.cakeit.cakitandroid.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cakeit.cakitandroid.data.source.local.entity.CakeShopData
import com.cakeit.cakitandroid.data.source.local.entity.SampleData
import com.cakeit.cakitandroid.data.source.local.room.dao.CakeShopDao
import com.cakeit.cakitandroid.data.source.local.room.dao.SampleDao

@Database(entities = arrayOf(SampleData::class, CakeShopData::class), version = 1, exportSchema = false)
abstract class CakeItDatabase : RoomDatabase() {
    abstract fun sampleDao() : SampleDao
    abstract fun cakeShopDao() : CakeShopDao

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
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}