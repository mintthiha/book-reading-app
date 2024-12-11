package com.example.bookreadingapp.domain

import com.example.bookreadingapp.data.entities.Heading
import com.example.bookreadingapp.data.entities.Image
import com.example.bookreadingapp.data.entities.Paragraph
import com.example.bookreadingapp.data.entities.TableData

// Mostly used when fetching from the database
sealed class ChapterElement() {
    data class ParagraphElement(val paragraph: Paragraph): ChapterElement()
    data class HeadingElement(val heading: Heading): ChapterElement()
    data class ImageElement(val image: Image): ChapterElement()
    data class TableElement(val table: TableData): ChapterElement()
}
