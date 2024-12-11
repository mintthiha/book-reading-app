package com.example.bookreadingapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookreadingapp.data.entities.Heading
/**
 * References for use of "suspense" and dao structure:
 *  https://developer.android.com/training/data-storage/room/async-queries#flow-coroutines
 *  Week 12 part-2 Word document
 */
@Dao
interface HeadingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHeading(heading: Heading): Long
    /*
    I-> Heading(headingId = 0, chapterId = 1, textHeading = "Introduction", orderIndex = 0)
    O-> Inserts the heading into the database
    */

    @Query("SELECT * FROM headings WHERE chapter_id = :chapterId ORDER BY order_index")
    fun getHeadingsByChapterIdLive(chapterId: Int): LiveData<List<Heading>>
    /*
    I-> chapterId = 1
    O-> LiveData containing:
    [
        Heading(headingId = 1, chapterId = 1, textHeading = "Introduction", orderIndex = 0),
        Heading(headingId = 2, chapterId = 1, textHeading = "Subheading", orderIndex = 1)
    ]
    */

    @Query("SELECT * FROM headings WHERE chapter_id = :chapterId ORDER BY order_index")
    fun getHeadingsByChapterId(chapterId: Int): List<Heading>
    /*
    I-> chapterId = 1
    O-> List containing:
    [
        Heading(headingId = 1, chapterId = 1, textHeading = "Introduction", orderIndex = 0),
        Heading(headingId = 2, chapterId = 1, textHeading = "Subheading", orderIndex = 1)
    ]
    */

    @Query("SELECT * FROM headings WHERE chapter_id = :chapterId AND text_heading LIKE '%' || :occurrence || '%' ORDER BY order_index")
    suspend fun getHeadingsByChapterIdWithOccurrence(chapterId: Int, occurrence: String): List<Heading>
    /*
    I-> chapterId = 1, occurrence = "in"
    O-> List containing:
    [
        Heading(headingId = 1, chapterId = 1, textHeading = "Introduction", orderIndex = 0),
        Heading(headingId = 2, chapterId = 1, textHeading = "Subheading", orderIndex = 1)
    ]
    */

    @Query("SELECT text_heading FROM headings WHERE chapter_id = :chapterId ORDER BY order_index")
    fun getAllHeadingTextsLive(chapterId: Int): LiveData<List<String>>
    /*
    I-> chapterId = 1
    O-> LiveData containing:
    ["Introduction", "Subheading"]
    */

    @Query("DELETE FROM headings WHERE chapter_id = :chapterId")
    suspend fun deleteHeadingsByChapterId(chapterId: Int)
    /*
    Deletes all headings in a chapter.
    */

    @Query("SELECT COUNT(*) FROM headings WHERE chapter_id = :chapterId")
    fun countHeadingsByChapterIdLive(chapterId: Int): LiveData<Int>
    /*
    I-> chapterId = 1
    O-> LiveData containing: 2 (Number of headings in chapterId 1)
    */
}
