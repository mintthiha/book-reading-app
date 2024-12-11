package com.example.bookreadingapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bookreadingapp.dao.ChapterDao
import com.example.bookreadingapp.data.entities.Chapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * References :
 *  Lecture Week 12: SQLite and Room Databases
 *  Lecture Week 12 Part 2 : Week 12 Part 2: Room Database Demo Project
 *  Week 12 part-2 Word document
 */
class ChapterRepository(private val chapterDao: ChapterDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // Current chapter
    private val _chapterLive = MutableLiveData<Chapter?>(null)
    val chapterLive: LiveData<Chapter?>
        get() = _chapterLive

    suspend fun insertChapter(chapter: Chapter): Long {
        return chapterDao.insertChapter(chapter)
    }

    fun getChaptersByBookIdLive(bookId: Int): LiveData<List<Chapter>> {
        return chapterDao.getChaptersByBookIdLive(bookId)
    }

    // Function to update the LiveData above to a chapter with the following orderIndex
    // belonging to book with bookId
    fun updateChapterByBookIdWithOrderIndex(bookId: Int, orderIndex: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            val chapter = chapterDao.getChapterByBookIdWithOrderIndex(bookId, orderIndex)
            _chapterLive.postValue(chapter)
        }
    }

    fun getChapterById(chapterId: Int, callback: (Chapter?) -> Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val chapter = chapterDao.getChapterById(chapterId)
            withContext(Dispatchers.Main) {
                callback(chapter)
            }
        }
    }

    fun deleteChaptersByBookId(bookId: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            chapterDao.deleteChaptersByBookId(bookId)
        }
    }

    fun getChapterTitle(chapterId: Int, callback: (String?) -> Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val title = chapterDao.getChapterTitle(chapterId)
            withContext(Dispatchers.Main) {
                callback(title)
            }
        }
    }

    fun getChapterTitlesByBookIdLive(bookId: Int): LiveData<List<String>> {
        return chapterDao.getChapterTitlesByBookIdLive(bookId)
    }

    fun getChapterCountByBookIdLive(bookId: Int): LiveData<Int> {
        return chapterDao.getChapterCountByBookIdLive(bookId)
    }

    fun deleteAllChapters() {
        coroutineScope.launch(Dispatchers.IO) {
            chapterDao.deleteAllChapters()
        }
    }
}
