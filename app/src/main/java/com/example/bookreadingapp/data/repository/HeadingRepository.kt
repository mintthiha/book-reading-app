package com.example.bookreadingapp.data.repository

import androidx.lifecycle.LiveData
import com.example.bookreadingapp.data.dao.HeadingDao
import com.example.bookreadingapp.data.entities.Heading
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
/**
 * References :
 *  Lecture Week 12: SQLite and Room Databases
 *  Lecture Week 12 Part 2 : Week 12 Part 2: Room Database Demo Project
 *  Week 12 part-2 Word document
 */
class HeadingRepository(private val headingDao: HeadingDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    suspend fun insertHeading(heading: Heading): Long {
        return headingDao.insertHeading(heading)
    }

    fun getHeadingsByChapterIdLive(chapterId: Int): LiveData<List<Heading>> {
        return headingDao.getHeadingsByChapterIdLive(chapterId)
    }

    fun getAllHeadingTextsLive(chapterId: Int): LiveData<List<String>> {
        return headingDao.getAllHeadingTextsLive(chapterId)
    }

    fun deleteHeadingsByChapterId(chapterId: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            headingDao.deleteHeadingsByChapterId(chapterId)
        }
    }

    fun countHeadingsByChapterIdLive(chapterId: Int): LiveData<Int> {
        return headingDao.countHeadingsByChapterIdLive(chapterId)
    }

}
