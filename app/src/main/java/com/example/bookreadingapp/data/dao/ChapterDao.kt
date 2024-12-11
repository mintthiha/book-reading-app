package com.example.bookreadingapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookreadingapp.data.entities.Chapter

/**
 * References for use of "suspense" and dao structure:
 *  https://developer.android.com/training/data-storage/room/async-queries#flow-coroutines
 *  Week 12 part-2 Word document
 */
@Dao
interface ChapterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapter(chapter: Chapter): Long
    /*
    I-> Chapter(chapterId = 1, bookId = 1, title = "Introduction", orderIndex = 0)
    O-> 1 (ID of the inserted chapter)
    */

    @Query("SELECT * FROM chapters WHERE book_id = :bookId ORDER BY order_index")
    fun getChaptersByBookIdLive(bookId: Int): LiveData<List<Chapter>>
    /*
    I-> bookId = 1
    O-> LiveData containing:
    [
        Chapter(1, 1, "Introduction", 0),
        Chapter(2, 1, "Chapter 1", 1)
    ]
    */

    @Query("SELECT * FROM chapters WHERE book_id = :bookId ORDER BY order_index")
    suspend fun getChaptersByBookId(bookId: Int): List<Chapter>
    /*
    I-> bookId = 1
    O-> List containing:
    [
        Chapter(1, 1, "Introduction", 0),
        Chapter(2, 1, "Chapter 1", 1)
    ]
    */

    @Query("SELECT title_chapter FROM chapters WHERE book_id = :bookId ORDER BY order_index ASC")
    fun getChapterTitlesByBookIdLive(bookId: Int): LiveData<List<String>>
    /*
    I-> bookId = 1
    O-> LiveData containing:
    ["Introduction", "Chapter 1"...]
    */

    @Query("SELECT * FROM chapters WHERE book_id = :bookId AND order_index = :orderIndex")
    suspend fun getChapterByBookIdWithOrderIndex(bookId: Int, orderIndex: Int): Chapter
    /*
    I-> bookId = 1
    O->  Chapter(2, 1, "Chapter 1", 1)
     */

    @Query("SELECT * FROM chapters WHERE chapter_id = :chapterId")
    suspend fun getChapterById(chapterId: Int): Chapter
    /*
    I-> chapterId = 1
    O->  Chapter(1, 1, "Introduction", 0)
    */

    @Query("DELETE FROM chapters WHERE book_id = :bookId")
    suspend fun deleteChaptersByBookId(bookId: Int)
    /*
    I-> bookId = 1
    O->  Deletes all chapters with bookId 1.
    */

    @Query("SELECT title_chapter FROM chapters WHERE chapter_id = :chapterId")
    suspend fun getChapterTitle(chapterId: Int): String?
    /*
    I-> chapterId = 1
    O-> "Introduction"
    */

    @Query("SELECT COUNT(*) FROM chapters WHERE book_id = :bookId")
    fun getChapterCountByBookIdLive(bookId: Int): LiveData<Int>
    /*
    I-> bookId = 1
    O-> LiveData containing: 2
    */

    @Query("DELETE FROM chapters")
    suspend fun deleteAllChapters()
    /*
    O-> Deletes all chapters from the database.
    */
}
