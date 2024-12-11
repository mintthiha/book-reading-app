package com.example.bookreadingapp.data.repository

import com.example.bookreadingapp.dao.ParagraphDao
import com.example.bookreadingapp.data.entities.Paragraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
/**
 * References :
 *  Lecture Week 12: SQLite and Room Databases
 *  Lecture Week 12 Part 2 : Week 12 Part 2: Room Database Demo Project
 *  Week 12 part-2 Word document
 */
class ParagraphRepository(private val paragraphDao: ParagraphDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    suspend fun insertParagraph(paragraph: Paragraph): Long {
        return paragraphDao.insertParagraph(paragraph)
    }

    fun getParagraphsByChapterId(chapterId: Int, callback: (List<Paragraph>) -> Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val paragraphs = paragraphDao.getParagraphsByChapterId(chapterId)
            coroutineScope.launch(Dispatchers.Main) {
                callback(paragraphs)
            }
        }
    }

    fun deleteParagraphsByChapterId(chapterId: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            paragraphDao.deleteParagraphsByChapterId(chapterId)
        }
    }
}
