package com.example.bookreadingapp.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreadingapp.data.BookReadingDatabase
import com.example.bookreadingapp.data.repository.FileRepository
import com.example.bookreadingapp.data.repository.HeadingRepository
import com.example.bookreadingapp.data.repository.ImageRepository
import com.example.bookreadingapp.data.repository.ParagraphRepository
import com.example.bookreadingapp.data.repository.TableRepository
import com.example.bookreadingapp.domain.BookModel
import com.example.bookreadingapp.repository.BookRepository
import com.example.bookreadingapp.repository.ChapterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class BookProcessingViewModel(application: Application) : ViewModel() {
    private val bookRepo: BookRepository
    private val chapterRepo: ChapterRepository
    private val headingRepo: HeadingRepository
    private val imageRepo: ImageRepository
    private val paragraphRepo: ParagraphRepository
    private val tableRepo: TableRepository
    private val fileRepo: FileRepository

    init {
        // Initialize database related components
        val roomDB = BookReadingDatabase.getInstance(application)
        val bookDao = roomDB.bookDao()
        bookRepo = BookRepository(bookDao)
        val chapterDao = roomDB.chapterDao()
        chapterRepo = ChapterRepository(chapterDao)
        val headingDao = roomDB.headingDao()
        headingRepo = HeadingRepository(headingDao)
        val imageDao = roomDB.imageDao()
        imageRepo = ImageRepository(imageDao)
        val paragraphDao = roomDB.paragraphDao()
        paragraphRepo = ParagraphRepository(paragraphDao)
        val tableDao = roomDB.tableDao()
        tableRepo = TableRepository(tableDao)
        fileRepo = FileRepository(application)
    }

    private val _downloadProgress = MutableLiveData<Int>()
    val downloadProgress: LiveData<Int> get() = _downloadProgress

    /**
     * Downloads a zip file from the given URL and unzips it into a directory.
     * Parses the unzipped HTML file and populate the database.
     *
     * @param url The URL of the zipped file to be downloaded.
     */
    fun downloadBook(url: String) {
        viewModelScope.launch {
            try {
                // Download file
                val bookFolder: File? = withContext(Dispatchers.IO) {
                    fileRepo.downloadAndUnzipBook(url, "DownloadedBooks") { progress ->
                        _downloadProgress.postValue(progress)
                    }
                }
                // Get a BookModel from downloaded file
                val bookModel: BookModel?
                if (bookFolder != null) {
                    bookModel = fileRepo.parseAndStoreBook(bookFolder = bookFolder)
                } else {
                    throw IllegalArgumentException("Book folder should not be null.")
                }
                // Insert into database a whole book using a BookModel
                if (bookModel != null) {
                    insertBookIntoDatabase(bookModel)
                } else {
                    throw IllegalArgumentException("Book model should not be null.")
                }
            } catch (e: Exception) {
                Log.e("BookProcessingViewModel", "Error downloading book: ${e.message}")
            }
        }
    }

    // Helper function that goes through a BookModel to insert into the database correctly
    private suspend fun insertBookIntoDatabase(bookModel: BookModel) {
        // Insert book
        val bookId = bookRepo.insertBook(bookModel.book)
        bookModel.chapters.forEachIndexed { index, chapter ->
            // Insert chapter
            val chapterId = chapterRepo.insertChapter(chapter.copy(bookId = bookId.toInt()))
            val chapterContents = bookModel.chapterContents[index]
            chapterContents.paragraphs.forEach { paragraph ->
                // Insert paragraph
                paragraphRepo.insertParagraph(paragraph.copy(chapterId = chapterId.toInt()))
            }
            chapterContents.headings.forEach { heading ->
                // Insert heading
                headingRepo.insertHeading(heading.copy(chapterId = chapterId.toInt()))
            }
            chapterContents.images.forEach { image ->
                // Insert image
                imageRepo.insertImage(image.copy(chapterId = chapterId.toInt()))
            }
            chapterContents.tables.forEach { table ->
                // Insert table
                tableRepo.insertTableData(table.copy(chapterId = chapterId.toInt()))
            }
        }
    }
}