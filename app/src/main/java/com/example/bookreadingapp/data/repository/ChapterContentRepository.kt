package com.example.bookreadingapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bookreadingapp.dao.ParagraphDao
import com.example.bookreadingapp.data.dao.HeadingDao
import com.example.bookreadingapp.data.dao.ImageDao
import com.example.bookreadingapp.data.dao.TableDao
import com.example.bookreadingapp.domain.ChapterElement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * References :
 *  Lecture Week 12: SQLite and Room Databases
 *  Lecture Week 12 Part 2 : Week 12 Part 2: Room Database Demo Project
 *  Week 12 part-2 Word document
 */
class ChapterContentRepository(
    private val headingDao: HeadingDao,
    private val paragraphDao: ParagraphDao,
    private val tableDao: TableDao,
    private val imageDao: ImageDao
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // List of ChapterElements for one specific chapter
    private val _chapterContentLive = MutableLiveData<List<ChapterElement>?>(null)
    val chapterContentLive: LiveData<List<ChapterElement>?>
        get() = _chapterContentLive

    // Function to update the live data above based on a new chapterId
    fun updateCurrentChapterElements(chapterId: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            // Fetch all types of elements
            val headings = headingDao.getHeadingsByChapterId(chapterId)
            val paragraphs = paragraphDao.getParagraphsByChapterId(chapterId)
            val images = imageDao.getImagesByChapterId(chapterId)
            val tables = tableDao.getTablesByChapterId(chapterId)
            // Convert all types of elements to a ChapterElement
            val headingElements = headings.map { heading ->
                ChapterElement.HeadingElement(heading)
            }
            val paragraphElements = paragraphs.map { paragraph ->
                ChapterElement.ParagraphElement(paragraph)
            }
            val imageElements = images.map { image ->
                ChapterElement.ImageElement(image)
            }
            val tableElements = tables.map { table ->
                ChapterElement.TableElement(table)
            }
            // Combine all lists into one
            val chapterElements = headingElements + paragraphElements + imageElements + tableElements
            // Sort list by orderIndex
            val sortedChapterElements = chapterElements
                .sortedBy {
                    when (it) {
                        is ChapterElement.HeadingElement -> it.heading.orderIndex
                        is ChapterElement.ParagraphElement -> it.paragraph.orderIndex
                        is ChapterElement.ImageElement -> it.image.orderIndex
                        is ChapterElement.TableElement -> it.table.orderIndex
                    }
                }
            _chapterContentLive.postValue(sortedChapterElements)
        }
    }
}
