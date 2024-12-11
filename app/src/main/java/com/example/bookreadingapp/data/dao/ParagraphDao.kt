package com.example.bookreadingapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookreadingapp.data.entities.Paragraph

/**
 * References for use of "suspense" and dao structure:
 *  https://developer.android.com/training/data-storage/room/async-queries#flow-coroutines
 *  Week 12 part-2 Word document
 */
@Dao
interface ParagraphDao {
    /**
     * I-> Paragraph(chapterId = 1, orderIndex = 0, textParagraph = "This is a paragraph.")
     * O-> ID of inserted paragraph.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParagraph(paragraph: Paragraph): Long

    /**
     * I-> chapterId = 1
     * O-> List of Paragraphs: [
     *      Paragraph(chapterId = 1, orderIndex = 0, textParagraph = "First paragraph."),
     *      Paragraph(chapterId = 1, orderIndex = 1, textParagraph = "Second paragraph.")
     * ]
     */
    @Query("SELECT * FROM paragraphs WHERE chapter_id = :chapterId ORDER BY order_index")
    suspend fun getParagraphsByChapterId(chapterId: Int): List<Paragraph>

    /**
     * Searches paragraphs in a specific chapter for a query string.
     * I-> chapterId = 1, occurrence = "important"
     * O-> List of Paragraphs containing "important":
     * [
     *      Paragraph(chapterId = 1, orderIndex = 2, textParagraph = "This is an important point.")
     * ]
     */
    @Query("SELECT * FROM paragraphs WHERE chapter_id = :chapterId AND text_paragraph LIKE '%' || :occurrence || '%' ORDER BY order_index")
    suspend fun getParagraphsByChapterIdWithOccurrence(chapterId: Int, occurrence: String): List<Paragraph>

    /**
     * Deletes all paragraphs for a given chapter ID.
     */
    @Query("DELETE FROM paragraphs WHERE chapter_id = :chapterId")
    suspend fun deleteParagraphsByChapterId(chapterId: Int)
}
