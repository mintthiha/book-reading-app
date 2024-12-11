package com.example.bookreadingapp.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
/**
 * References :
 *  Lecture Week 12: SQLite and Room Databases
 *  Week 12 part-2 Word document
 */
@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "book_id")
    val bookId: Int = 0,

    @ColumnInfo(name = "title_book")
    val title: String,

    @ColumnInfo(name = "author")
    val author: String,

    @ColumnInfo(name = "cover_img_path")
    val coverImgPath: String,

)
