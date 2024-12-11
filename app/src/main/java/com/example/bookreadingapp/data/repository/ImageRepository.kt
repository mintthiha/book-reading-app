package com.example.bookreadingapp.data.repository

import androidx.lifecycle.LiveData
import com.example.bookreadingapp.data.dao.ImageDao
import com.example.bookreadingapp.data.entities.Image
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
/**
 * References :
 *  Lecture Week 12: SQLite and Room Databases
 *  Lecture Week 12 Part 2 : Week 12 Part 2: Room Database Demo Project
 *  Week 12 part-2 Word document
 */
class ImageRepository(private val imageDao: ImageDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    suspend fun insertImage(image: Image): Long {
        return imageDao.insertImage(image)
    }

    fun getImagesByChapterId(chapterId: Int, callback: (List<Image>) -> Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val images = imageDao.getImagesByChapterId(chapterId)
            coroutineScope.launch(Dispatchers.Main) {
                callback(images)
            }
        }
    }

    fun deleteImagesByChapterId(chapterId: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            imageDao.deleteImagesByChapterId(chapterId)
        }
    }

    fun countImagesByChapterIdLive(chapterId: Int): LiveData<Int> {
        return imageDao.countImagesByChapterIdLive(chapterId)
    }

}
