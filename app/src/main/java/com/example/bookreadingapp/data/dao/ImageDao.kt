package com.example.bookreadingapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookreadingapp.data.entities.Image
/**
 * References for use of "suspense" and dao structure:
 *  https://developer.android.com/training/data-storage/room/async-queries#flow-coroutines
 *  Week 12 part-2 Word document
 */
@Dao
interface ImageDao {

    /**
     * I-> Image(chapterId = 1, orderIndex = 0, imagePath = "/images/diagram.png")
     * O-> Inserts the image and replaces if conflict occur
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: Image): Long

    /**
     * I-> chapterId = 1
     * O-> List of Images: [
     *      Image(chapterId = 1, orderIndex = 0, imagePath = "/images/diagram1.png"),
     *      Image(chapterId = 1, orderIndex = 1, imagePath = "/images/diagram2.png")
     * ]
     */
    @Query("SELECT * FROM images WHERE chapter_id = :chapterId ORDER BY order_index")
    suspend fun getImagesByChapterId(chapterId: Int): List<Image>

    /**
     * I-> chapterId = 1
     * O-> Deletes all images with chapterId 1
     */
    @Query("DELETE FROM images WHERE chapter_id = :chapterId")
    suspend fun deleteImagesByChapterId(chapterId: Int)

    /**
     * Counts the number of images for a given chapter ID.
     * I-> chapterId = 1
     * O-> 2 (number of images in chapterId 1)
     */
    @Query("SELECT COUNT(*) FROM images WHERE chapter_id = :chapterId")
    fun countImagesByChapterIdLive(chapterId: Int): LiveData<Int>
}
