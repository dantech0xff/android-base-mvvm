package com.creative.mvvm.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.creative.mvvm.data.note.dao.NoteDao
import com.creative.mvvm.data.note.entity.Note

const val VERSION = 1

@Database(
    entities = [Note::class],
    version = VERSION,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        // Check for DB instance if not null then get or insert or else create new DB Instance
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {

            instance ?: createDatabase(context).also { instance = it }
        }

        // create db instance
        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_db.db"
        ).build()
    }
}