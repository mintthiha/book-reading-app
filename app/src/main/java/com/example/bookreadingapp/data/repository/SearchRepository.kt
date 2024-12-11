package com.example.bookreadingapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bookreadingapp.dao.ChapterDao
import com.example.bookreadingapp.dao.ParagraphDao
import com.example.bookreadingapp.data.dao.HeadingDao
import com.example.bookreadingapp.data.entities.Chapter
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
class SearchRepository(
    private val chapterDao: ChapterDao,
    private val headingDao: HeadingDao,
    private val paragraphDao: ParagraphDao,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // Map of chapter and their elements
    private val _chapterToElementsMapLive = MutableLiveData<Map<Chapter, List<ChapterElement>>?>(null)
    val chapterToElementsMapLive: LiveData<Map<Chapter, List<ChapterElement>>?>
        get() = _chapterToElementsMapLive

    // Function to update the live data above based on a new bookId and occurrence
    fun updateChapterElementsWithOccurrenceByBookId(bookId: Int, occurrence: String) {
        coroutineScope.launch(Dispatchers.IO) {
            val chapterToElementsMap = mutableMapOf<Chapter, List<ChapterElement>>()
            val chapters = chapterDao.getChaptersByBookId(bookId)
            for (chapter in chapters) {
                // Fetch all elements
                val headings = headingDao.getHeadingsByChapterIdWithOccurrence(chapter.chapterId, occurrence)
                val paragraphs = paragraphDao.getParagraphsByChapterIdWithOccurrence(chapter.chapterId, occurrence)
                // Convert all elements into a ChapterElement
                val headingElements = headings.map { heading ->
                    ChapterElement.HeadingElement(heading)
                }
                val paragraphElements = paragraphs.map { paragraph ->
                    ChapterElement.ParagraphElement(paragraph)
                }
                // Combine all elements
                val chapterElements = headingElements + paragraphElements
                // Sort ChapterElements by orderIndex
                val sortedChapterElements = chapterElements
                    .sortedBy {
                        when (it) {
                            is ChapterElement.HeadingElement -> it.heading.orderIndex
                            is ChapterElement.ParagraphElement -> it.paragraph.orderIndex
                            // The following two are required by Kotlin, but will never be used
                            is ChapterElement.ImageElement -> it.image.orderIndex
                            is ChapterElement.TableElement -> it.table.orderIndex
                        }
                    }
                // Only add to the map if the list of chapter elements is not empty
                if (sortedChapterElements.isNotEmpty()) {
                    chapterToElementsMap[chapter] = sortedChapterElements
                }
            }
            _chapterToElementsMapLive.postValue(chapterToElementsMap)
        }
    }
}
