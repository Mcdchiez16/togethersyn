package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.CallEntity
import com.example.data.model.MessageEntity
import com.example.data.model.MomentEntity

@Database(
    entities = [MessageEntity::class, CallEntity::class, MomentEntity::class],
    version = 3,
    exportSchema = false
)
abstract class TogetherDatabase : RoomDatabase() {
    abstract fun togetherDao(): TogetherDao

    companion object {
        @Volatile
        private var INSTANCE: TogetherDatabase? = null

        fun getDatabase(context: Context): TogetherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TogetherDatabase::class.java,
                    "together_sync_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
