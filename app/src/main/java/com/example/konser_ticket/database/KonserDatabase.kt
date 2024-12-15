package com.example.konser_ticket.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [KonserRoom::class], version = 1, exportSchema = false)
abstract class KonserDatabase : RoomDatabase() {
    abstract fun KonserDao(): KonserDao

    companion object {
        @Volatile
        private var INSTANCE: KonserDatabase? = null
        fun getDatabase(context: Context): KonserDatabase? {
            if (INSTANCE == null) {
                synchronized(KonserDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        KonserDatabase::class.java,
                        "Konser_database"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}