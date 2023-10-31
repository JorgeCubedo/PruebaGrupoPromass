package com.cube

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class Entry {

     @PrimaryKey
     lateinit var id: String;
     @ColumnInfo(name = "title")
     lateinit var title: String;
     @ColumnInfo(name = "author")
     lateinit var author: String;
     @ColumnInfo(name = "posted_date")
     lateinit var postedDate: String;
     @ColumnInfo(name = "content")
     lateinit var content: String;


}