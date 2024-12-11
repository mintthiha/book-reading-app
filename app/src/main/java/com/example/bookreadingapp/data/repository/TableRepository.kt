package com.example.bookreadingapp.data.repository

import androidx.lifecycle.LiveData
import com.example.bookreadingapp.data.dao.TableDao
import com.example.bookreadingapp.data.entities.TableData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
/**
 * References :
 *  Lecture Week 12: SQLite and Room Databases
 *  Lecture Week 12 Part 2 : Week 12 Part 2: Room Database Demo Project
 *  Week 12 part-2 Word document
 */
class TableRepository(private val tableDao: TableDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    suspend fun insertTableData(tableData: TableData): Long {
        return tableDao.insertTableData(tableData)
    }

    fun insertTableDataList(tableDataList: List<TableData>) {
        coroutineScope.launch(Dispatchers.IO) {
            tableDao.insertTableDataList(tableDataList)
        }
    }

    fun getTableById(tableId: Int, callback: (TableData?) -> Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val table = tableDao.getTableById(tableId)
            coroutineScope.launch(Dispatchers.Main) {
                callback(table)
            }
        }
    }

    fun getTableHtmlByChapterIdLive(chapterId: Int): LiveData<List<String>> {
        return tableDao.getTableHtmlByChapterIdLive(chapterId)
    }

    fun countTablesByChapterIdLive(chapterId: Int): LiveData<Int> {
        return tableDao.countTablesByChapterIdLive(chapterId)
    }

    fun deleteTablesByChapterId(chapterId: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            tableDao.deleteTablesByChapterId(chapterId)
        }
    }
}
