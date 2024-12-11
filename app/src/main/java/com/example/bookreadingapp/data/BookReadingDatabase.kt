package com.example.bookreadingapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bookreadingapp.dao.ChapterDao
import com.example.bookreadingapp.dao.ParagraphDao
import com.example.bookreadingapp.data.dao.BookDao
import com.example.bookreadingapp.data.dao.HeadingDao
import com.example.bookreadingapp.data.dao.ImageDao
import com.example.bookreadingapp.data.dao.TableDao
import com.example.bookreadingapp.data.entities.Book
import com.example.bookreadingapp.data.entities.Chapter
import com.example.bookreadingapp.data.entities.Heading
import com.example.bookreadingapp.data.entities.Image
import com.example.bookreadingapp.data.entities.Paragraph
import com.example.bookreadingapp.data.entities.TableData

/**
 * References :
 *  Lecture Week 12 Part 2 : Week 12 Part 2: Room Database Demo Project
 *  Week 12 part-2 Word document
 */
@Database(
    entities = [
        Book::class,
        Chapter::class,
        Paragraph::class,
        Heading::class,
        Image::class,
        TableData::class
    ],
    version = 2,
    exportSchema = true
)
abstract class BookReadingDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun chapterDao(): ChapterDao
    abstract fun paragraphDao(): ParagraphDao
    abstract fun headingDao(): HeadingDao
    abstract fun imageDao(): ImageDao
    abstract fun tableDao(): TableDao

    companion object {
        @Volatile
        private var INSTANCE: BookReadingDatabase? = null

        fun getInstance(context: Context): BookReadingDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookReadingDatabase::class.java,
                    "book_reading_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}

