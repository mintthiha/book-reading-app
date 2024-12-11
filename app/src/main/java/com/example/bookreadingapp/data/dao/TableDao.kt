package com.example.bookreadingapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookreadingapp.data.entities.TableData
/**
 * References for use of "suspense" and dao structure:
 *  https://developer.android.com/training/data-storage/room/async-queries#flow-coroutines
 *  Week 12 part-2 Word document
 */

@Dao
interface TableDao {
    /**
     * I-> TableData(tableId = 0, chapterId = 1, orderIndex = 0, tableHtml = "<table>...</table>")
     * O-> Inserts the entry into the tables table.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTableData(tableData: TableData): Long

    /**
     * I-> List of TableData:
     * [
     *      TableData(tableId = 0, chapterId = 1, orderIndex = 0, tableHtml = "<table>...</table>"),
     *      TableData(tableId = 0, chapterId = 1, orderIndex = 1, tableHtml = "<table>...</table>")
     * ]
     * O-> Inserts all entries into the tables table.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTableDataList(tableDataList: List<TableData>)

    /**
     * I-> chapterId = 1
     * O-> List of TableData:
     * [
     *      TableData(tableId = 1, chapterId = 1, orderIndex = 0, tableHtml = "<table>...</table>"),
     *      TableData(tableId = 2, chapterId = 1, orderIndex = 1, tableHtml = "<table>...</table>")
     * ]
     */
    @Query("SELECT * FROM tables WHERE chapter_id = :chapterId ORDER BY order_index")
    fun getTablesByChapterId(chapterId: Int): List<TableData>

    /**
     * I-> tableId = 1
     * O-> TableData(tableId = 1, chapterId = 1, orderIndex = 0, tableHtml = "<table>...</table>")
     */
    @Query("SELECT * FROM tables WHERE table_id = :tableId")
    suspend fun getTableById(tableId: Int): TableData?

    /**
     * I-> chapterId = 1
     * O-> List of table HTML:
     * [
     *      "<table>...</table>",
     *      "<table>...</table>"
     * ]
     */
    @Query("SELECT table_html FROM tables WHERE chapter_id = :chapterId ORDER BY order_index")
    fun getTableHtmlByChapterIdLive(chapterId: Int): LiveData<List<String>>

    /**
     * I-> chapterId = 1
     * O-> LiveData containing: 2
     */
    @Query("SELECT COUNT(*) FROM tables WHERE chapter_id = :chapterId")
    fun countTablesByChapterIdLive(chapterId: Int): LiveData<Int>

    /**
     * I-> chapterId = 1
     * O-> Deletes all tables with chapterId 1 from the database.
     */
    @Query("DELETE FROM tables WHERE chapter_id = :chapterId")
    suspend fun deleteTablesByChapterId(chapterId: Int)
}