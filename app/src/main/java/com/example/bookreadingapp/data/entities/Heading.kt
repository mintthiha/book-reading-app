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
    tableName = "headings",
    foreignKeys = [ForeignKey(
        entity = Chapter::class,
        parentColumns = ["chapter_id"],
        childColumns = ["chapter_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Heading(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "heading_id")
    val headingId: Int = 0,

    @ColumnInfo(name = "chapter_id")
    val chapterId: Int,

    @ColumnInfo(name = "order_index")
    val orderIndex: Int,

    @ColumnInfo(name = "text_heading")
    val text: String
)
