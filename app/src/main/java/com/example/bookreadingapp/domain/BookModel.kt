package com.example.bookreadingapp.domain

import com.example.bookreadingapp.data.entities.Book
import com.example.bookreadingapp.data.entities.Chapter

data class BookModel (
    val book: Book,
    // Chapters and chaptersElements must be the same length
    val chapters: List<Chapter> = emptyList(),
    val chapterContents: List<ChapterContentModel> = emptyList()
)