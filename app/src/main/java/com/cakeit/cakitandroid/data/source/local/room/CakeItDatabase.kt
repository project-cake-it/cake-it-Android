package com.cakeit.cakitandroid.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cakeit.cakitandroid.data.source.local.entity.SampleData
import com.cakeit.cakitandroid.data.source.local.room.dao.SampleDao

@Database(entities = arrayOf(SampleData::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sampleDao() : SampleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cakeit_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}