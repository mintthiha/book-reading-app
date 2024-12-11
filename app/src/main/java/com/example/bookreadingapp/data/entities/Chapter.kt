package com.example.bookreadingapp.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
/**
 * References :
 *  Lecture Week 12: SQLite and Room Databases
 *  Week 12 part-2 Word document
 */
@Entity(
    tableName = "chapters",
    foreignKeys = [ForeignKey(
        entity = Book::class,
        parentColumns = ["book_id"],
        childColumns = ["book_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Chapter(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "chapter_id")
    val chapterId: Int = 0,

    @ColumnInfo(name = "book_id")
    val bookId: Int,

    @ColumnInfo(name = "title_chapter")
    val title: String,

    @ColumnInfo(name = "order_index")
    val orderIndex: Int
)
