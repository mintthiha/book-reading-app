package com.example.bookreadingapp.repository

import androidx.lifecycle.LiveData
import com.example.bookreadingapp.data.dao.BookDao
import com.example.bookreadingapp.data.entities.Book
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
/**
 * References :
 *  Lecture Week 12: SQLite and Room Databases
 *  Lecture Week 12 Part 2 : Week 12 Part 2: Room Database Demo Project
 *  Week 12 part-2 Word document
 */
class BookRepository(private val bookDao: BookDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    suspend fun insertBook(book: Book): Long {
        return bookDao.insertBook(book)
    }

    val allBooksLive: LiveData<List<Book>> = bookDao.getAllBooksLive()

    fun getAllBookTitlesLive(): LiveData<List<String>> {
        return bookDao.getAllBookTitlesLive()
    }

    fun findBookById(bookId: Int, callback: (Book?) -> Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val book = bookDao.getBook(bookId)
            coroutineScope.launch(Dispatchers.Main) {
                callback(book)
            }
        }
    }

    fun getBookTitle(bookId: Int, callback: (String?) -> Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val author = bookDao.getBookTitle(bookId)
            coroutineScope.launch(Dispatchers.Main) {
                callback(author)
            }
        }
    }

    fun getBookAuthor(bookId: Int, callback: (String?) -> Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val author = bookDao.getBookAuthor(bookId)
            coroutineScope.launch(Dispatchers.Main) {
                callback(author)
            }
        }
    }

    fun getBookCoverPath(bookId: Int, callback: (String?) -> Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val coverPath = bookDao.getBookCoverPath(bookId)
            coroutineScope.launch(Dispatchers.Main) {
                callback(coverPath)
            }
        }
    }

    fun deleteBookById(bookId: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            bookDao.deleteBookById(bookId)
        }
    }

    fun deleteAllBooks() {
        coroutineScope.launch(Dispatchers.IO) {
            bookDao.deleteAllBooks()
        }
    }

    fun getBookCountLive(): LiveData<Int> {
        return bookDao.getBookCountLive()
    }
}
