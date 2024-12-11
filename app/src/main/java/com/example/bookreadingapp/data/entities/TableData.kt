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
    tableName = "tables",
    foreignKeys = [ForeignKey(
        entity = Chapter::class,
        parentColumns = ["chapter_id"],
        childColumns = ["chapter_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TableData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "table_id")
    val tableId: Int = 0,

    @ColumnInfo(name = "chapter_id")
    val chapterId: Int,

    @ColumnInfo(name = "order_index")
    val orderIndex: Int,

    @ColumnInfo(name = "table_html")
    val tableHtml: String
)
