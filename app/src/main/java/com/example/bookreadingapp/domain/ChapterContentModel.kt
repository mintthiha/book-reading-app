package com.example.bookreadingapp.domain

import com.example.bookreadingapp.data.entities.Heading
import com.example.bookreadingapp.data.entities.Image
import com.example.bookreadingapp.data.entities.Paragraph
import com.example.bookreadingapp.data.entities.TableData

// Mostly used when inserting into the database
data class ChapterContentModel(
    val tables: List<TableData> = emptyList(),
    val headings: List<Heading> = emptyList(),
    val paragraphs: List<Paragraph> = emptyList(),
    val images: List<Image> = emptyList()
)
