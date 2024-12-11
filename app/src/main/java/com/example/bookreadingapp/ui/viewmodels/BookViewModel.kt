package com.example.bookreadingapp.ui.viewmodels

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreadingapp.data.BookReadingDatabase
import com.example.bookreadingapp.data.entities.Book
import com.example.bookreadingapp.data.entities.Chapter
import com.example.bookreadingapp.data.repository.ChapterContentRepository
import com.example.bookreadingapp.data.repository.SearchRepository
import com.example.bookreadingapp.domain.ChapterElement
import com.example.bookreadingapp.repository.BookRepository
import com.example.bookreadingapp.repository.ChapterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel for managing the state and operations related to books and chapters in the Book Reading App.
 * Handles the retrieval, update, and storage of book and chapter data, as well as updating the current chapter.
 * Also interacts with the Room database and SharedPreferences to persist app data.
 *
 * @param application The application context to access resources such as databases.
 * @param sharedPreferences The SharedPreferences for storing app preferences, including the current book and chapter information.
 */
class BookViewModel(application: Application, sharedPreferences: SharedPreferences) : ViewModel() {

    // --- Repositories ---
    private val _bookRepo: BookRepository
    private val _chapterRepo: ChapterRepository
    private val _searchRepo: SearchRepository
    private val _chapterContentRepo: ChapterContentRepository

    // --- LiveData ---
    /**
     * LiveData for all the books available in the database.
     */
    val allBooks: LiveData<List<Book>>

    /**
     * LiveData for mapping chapters to their corresponding chapter elements.
     */
    val chapterToElementsMapLive: LiveData<Map<Chapter, List<ChapterElement>>?>

    /**
     * LiveData for the current content (elements) of a chapter.
     */
    val chapterContentLive: LiveData<List<ChapterElement>?>

    /**
     * LiveData for the current chapter being viewed.
     */
    val currentChapter: LiveData<Chapter?>

    // --- Book and Chapter Information ---
    private var _currentBook: Book? = null
    /**
     * The current book that is being read.
     */
    val currentBook: Book?
        get() = _currentBook

    private var _currentChapterElementOrderIndex: Int? = null
    /**
     * The order index for the current chapter element (e.g., heading, paragraph).
     */
    val currentChapterElementOrderIndex: Int?
        get() = _currentChapterElementOrderIndex

    // --- SharedPreferences ---
    private val _sharedPreferences: SharedPreferences

    init {
        val roomDB = BookReadingDatabase.getInstance(application)

        // Initialize DAOs for accessing different entities in the database
        val bookDao = roomDB.bookDao()
        val chapterDao = roomDB.chapterDao()
        val headingDao = roomDB.headingDao()
        val paragraphDao = roomDB.paragraphDao()
        val imageDao = roomDB.imageDao()
        val tableDao = roomDB.tableDao()

        // Initialize repositories
        _bookRepo = BookRepository(bookDao)
        _chapterRepo = ChapterRepository(chapterDao)
        _searchRepo = SearchRepository(chapterDao, headingDao, paragraphDao)
        _chapterContentRepo = ChapterContentRepository(headingDao, paragraphDao, tableDao, imageDao)

        // Initialize LiveData
        allBooks = _bookRepo.allBooksLive
        chapterToElementsMapLive = _searchRepo.chapterToElementsMapLive
        chapterContentLive = _chapterContentRepo.chapterContentLive
        currentChapter = _chapterRepo.chapterLive

        _sharedPreferences = sharedPreferences
    }

    // --- Methods ---

    /**
     * Sets the current book that is being read and saves it to SharedPreferences.
     *
     * @param book The book to be set as the current book.
     */
    fun setCurrentBook(book: Book) {
        _currentBook = book
        val bookToString = book.bookId.toString() + "###" + book.title + "###" + book.author + "###" + book.coverImgPath
        _sharedPreferences.edit().putString("current_book", bookToString).apply()
    }

    /**
     * Retrieves the chapters for the current book.
     *
     * @return LiveData of a list of chapters for the current book.
     */
    fun getCurrentBookChapters(): LiveData<List<Chapter>> {
        return _chapterRepo.getChaptersByBookIdLive(currentBook!!.bookId)
    }

    /**
     * Retrieves the number of chapters for the current book.
     *
     * @return LiveData of the number of chapters for the current book.
     */
    fun getCurrentBookChapterCount(): LiveData<Int> {
        return _chapterRepo.getChapterCountByBookIdLive(currentBook!!.bookId)
    }

    /**
     * Updates the current chapter based on the provided order index and saves it to SharedPreferences.
     *
     * @param orderIndex The order index of the chapter to be set as the current chapter.
     */
    fun updateCurrentChapter(orderIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _chapterRepo.updateChapterByBookIdWithOrderIndex(_currentBook!!.bookId, orderIndex)
        }
        _sharedPreferences.edit().putInt("current_chapter_order_index", orderIndex).apply()
    }

    /**
     * Updates the content of the current chapter based on the chapter ID.
     *
     * @param chapterId The ID of the chapter to update the content for.
     */
    fun updateCurrentChapterContent(chapterId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _chapterContentRepo.updateCurrentChapterElements(chapterId)
        }
    }

    /**
     * Updates the chapter elements with occurrence data based on the search query.
     *
     * @param query The search query used to filter chapter elements.
     */
    fun updateCurrentBookChapterElementsWithOccurrence(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _searchRepo.updateChapterElementsWithOccurrenceByBookId(currentBook!!.bookId, query)
        }
    }

    /**
     * Sets the order index of the current chapter element and saves it to SharedPreferences.
     *
     * @param orderIndex The order index of the chapter element.
     */
    fun setCurrentChapterElementOrderIndex(orderIndex: Int) {
        if (orderIndex <= 0) {
            return
        }
        _currentChapterElementOrderIndex = orderIndex
        _sharedPreferences.edit().putInt("current_chapter_element_order_index", orderIndex).apply()
    }

    /**
     * Sets the order index of the current chapter element when scrolling and saves it to SharedPreferences.
     *
     * @param orderIndex The order index of the chapter element.
     */
    fun setScrollCurrentChapterElementOrderIndex(orderIndex: Int) {
        _sharedPreferences.edit().putInt("current_scroll_chapter_element_order_index", orderIndex).apply()
    }
}