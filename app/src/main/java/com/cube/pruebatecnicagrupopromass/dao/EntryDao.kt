package com.cube.pruebatecnicagrupopromass.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cube.Entry

@Dao
interface EntryDao {
    @Query("SELECT * FROM Entry")
    fun getAll(): List<Entry>

    @Insert
    fun insertEntry(entry: Entry)

    @Query("SELECT * FROM Entry Where id = :id")
    fun getEntry(id: String): Entry
}