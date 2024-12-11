package com.example.bookreadingapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookreadingapp.data.entities.Book

/**
 * References for use of "suspense" and dao structure:
 *  https://developer.android.com/training/data-storage/room/async-queries#flow-coroutines
 *  Week 12 part-2 Word document
 */

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book): Long
    /*
    I-> Book(bookId = 1, title = "My First Book", author = "Ivana", coverImgPath = "/images/pathblabla.png")
    O-> 1 (ID of the inserted book)
    */

    @Query("SELECT * FROM books")
    fun getAllBooksLive(): LiveData<List<Book>>
    /*
    O-> LiveData containing the following list:
    [
        Book(1, "My First Book", "Ivana", "/images/pathblabla.png" ),
        Book(2, "Another Book", "Maya","/images/pathblabla2.png")
    ]
    */

    @Query("SELECT title_book FROM books")
    fun getAllBookTitlesLive(): LiveData<List<String>>
    /*
    O-> LiveData containing the following list:
    ["My First Book", "Another Book"]
    */

    @Query("SELECT * FROM books WHERE book_id = :bookId")
    suspend fun getBook(bookId: Int): Book?
    /*
    I-> bookId = 1
    O->  Book(1, "My First Book", "Ivana", "/images/pathblabla.png")
    */

    @Query("SELECT title_book FROM books WHERE book_id = :bookId")
    suspend fun getBookTitle(bookId: Int): String?
    /*
    I->  bookId = 1
    O-> "My First Book"
    */

    @Query("SELECT author FROM books WHERE book_id = :bookId")
    suspend fun getBookAuthor(bookId: Int): String?
    /*
    I->bookId = 1
    O-> "Ivana"
    */

    @Query("SELECT cover_img_path FROM books WHERE book_id = :bookId")
    suspend fun getBookCoverPath(bookId: Int): String?
    /*
    I-> bookId = 1
    O-> "/images/pathblabla.png"
    */

    @Query("DELETE FROM books WHERE book_id = :bookId")
    suspend fun deleteBookById(bookId: Int)
    /*
    I-> bookId = 1
    O-> Deletes the book with id 1
    */

    @Query("SELECT COUNT(*) FROM books")
    fun getBookCountLive(): LiveData<Int>
    /*
    O-> count of books:
    2
    */

    @Query("DELETE FROM books")
    suspend fun deleteAllBooks()
    /*
     O->  Deletes all books in the database.
    */
}
