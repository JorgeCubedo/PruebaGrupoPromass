package com.cube.pruebatecnicajorgecubedo.dao

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cube.Entry

@androidx.room.Database(entities = [Entry::class], version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun entryDao(): EntryDao


    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "dbPrueba.db"
                ).allowMainThreadQueries().build()
            }

            return instance as AppDatabase
        }
    }


}