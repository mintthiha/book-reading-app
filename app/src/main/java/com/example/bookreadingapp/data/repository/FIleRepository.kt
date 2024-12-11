package com.example.bookreadingapp.data.repository

import android.content.Context
import android.os.Environment
import android.util.Log
import com.example.bookreadingapp.data.entities.Book
import com.example.bookreadingapp.data.entities.Chapter
import com.example.bookreadingapp.data.entities.Heading
import com.example.bookreadingapp.data.entities.Image
import com.example.bookreadingapp.data.entities.Paragraph
import com.example.bookreadingapp.data.entities.TableData
import com.example.bookreadingapp.domain.BookModel
import com.example.bookreadingapp.domain.ChapterContentModel
import com.example.bookreadingapp.ui.utils.UnzipUtils
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

const val TAG = "FileRepository"

// All code is heavily inspired from Carlton's Lecture Week 13
open class FileRepository(private val context: Context) {
    fun downloadAndUnzipBook(url: String, directoryName: String, progressCallback: (Int) -> Unit): File? {
        val zipFileName = url.substringAfterLast("/")
        val zipFile = createFile(directoryName, zipFileName)
        val isDownloaded = downloadFile(url, zipFile, progressCallback)
        if (!isDownloaded) {
            return null
        }
        val unzippedDirectory = File(zipFile.parentFile, zipFileName.removeSuffix(".zip"))
        if (!unzippedDirectory.exists()) {
            unzippedDirectory.mkdirs()
        }
        try {
            // Unzips {zipFile} and stores its content in {unzippedDirectory}
            UnzipUtils.unzip(zipFile, unzippedDirectory.absolutePath)
            // Deletes the zip file
            deleteFile(zipFile)
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "An error occurred while unzipping ${zipFile.name}")
            return null
        }
        return unzippedDirectory
    }

    // List directory contents
    fun listDirectoryContents(directoryName: String): List<String> {
        val downloadFolder = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), directoryName)
        return downloadFolder.listFiles()?.map { it.name } ?: emptyList()
    }

    // Creates a file called {fileName} inside Downloads/{directoryName}
    private fun createFile(directoryName: String, fileName: String): File {
        val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), directoryName)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        return File(directory, fileName)
    }

    // Download file from the inputted URL and save it in the inputted file's location
    // Inspired by https://github.com/mitrejcevski/coroutineFileDownload/blob/master/app/src/main/java/nl/example/kts/DownloadViewModel.kt
    private fun downloadFile(url: String, file: File, progressCallback: (Int) -> Unit): Boolean {
        return try {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()

            if (!response.isSuccessful || response.body == null) {
                return false
            }

            val totalSize = response.body!!.contentLength()
            var downloadedSize = 0L

            val buffer = ByteArray(8 * 1024) // 8KB buffer size
            response.body!!.byteStream().use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                        downloadedSize += bytesRead
                        val progress = (downloadedSize * 100 / totalSize).toInt()
                        progressCallback(progress)
                    }
                }
            }

            true
        } catch (e: IOException) {
            Log.e(TAG, e.message ?: "An error occurred while downloading $url")
            false
        }
    }

    // Helper method to copy data from input to output stream
    private fun copyStream(input: InputStream, output: FileOutputStream) {
        val buffer = ByteArray(1024)
        var length: Int
        while (input.read(buffer).also { length = it } > 0) {
            output.write(buffer, 0, length)
        }
    }

    // Helper method to delete a file
    private fun deleteFile(file: File): Boolean {
        if (file.exists()) {
            val deleted = file.delete()
            if (!deleted) {
                Log.e(TAG, "Failed to delete the file: ${file.name}")
                return false
            }
        }
        return true
    }

    fun parseAndStoreBook(bookFolder: File): BookModel? {
        var coverImagePath = ""
        val coverImage = getCoverImageInDirectory(bookFolder)
        if (coverImage != null) {
            coverImagePath = coverImage.absolutePath
        } else {
            Log.e(TAG, "No valid cover image found in the unzipped directory")
            return null
        }
        val htmlFile = getHtmlFileInDirectory(bookFolder)
        if (htmlFile != null) {
            return parseAndStoreBookHtml(htmlFile, bookFolder.absolutePath, coverImagePath)
        } else {
            Log.e(TAG, "No valid HTML file found in the unzipped directory")
            return null
        }
    }

    private fun getCoverImageInDirectory(directory: File): File? {
        // All cover images can be assumed to end with -cover.png
        return directory.listFiles { file ->
            file.name.endsWith("-cover.png")
        }?.firstOrNull()
    }

    private fun getHtmlFileInDirectory(directory: File): File? {
        // All HTML files can be assumed to end with -images.html
        return directory.listFiles { file ->
            file.name.endsWith("-images.html")
        }?.firstOrNull()
    }

    // Helper method to parse through the elements of a HTML file
    // Stores the elements appropriately in the database
    private fun parseAndStoreBookHtml(htmlFile: File, currentPath: String, coverImagePath: String): BookModel {
        val document = Jsoup.parse(htmlFile, "UTF-8")

        // Create the book
        val title = document.selectFirst("h1")?.text() ?: ""
        val author = document.select("body > h2").first()?.text() ?: ""
        val book = Book(title = title, author = author, coverImgPath = coverImagePath)

        // Create the chapters
        val chapters: MutableList<Chapter> = mutableListOf()
        val chapterContents: MutableList<ChapterContentModel> = mutableListOf()

        val elements = document.body().children()
        var chapterCount = 0
        for (element in elements) {
            // Chapter called Contents containing a table
            if (element.tagName() == "h2" && element.text() == "Contents") {
                // It's safe to assume that a Contents h2 is always followed by a table element
                // It's safe to assume that Contents chapter only have table as its chapter content
                chapterCount++
                val tableElement: Element? = getChildTableElement(element)
                if (tableElement != null) {
                    chapters.add(Chapter(0, 0, "Contents", chapterCount))
                    val tempTableElements = listOf(
                        // toString() returns the actual HTML of the <table>
                        // while html() returns the table's <tbody>
                        TableData(0, 0, 1, tableElement.toString())
                    )
                    val tempChapterContent = ChapterContentModel(tables = tempTableElements)
                    chapterContents.add(tempChapterContent)
                }
            }
            // Any other chapter containing a mix of images, paragraphs, and headings
            if (element.tagName() == "div" && element.hasClass("chapter")) {
                chapterCount++
                val chapterTitle = element.selectFirst("h2")?.text() ?: ""
                chapters.add(Chapter(0, 0, chapterTitle, chapterCount))
                val tempChapterContent: ChapterContentModel = parseAndStoreChapterHtml(element, currentPath)
                chapterContents.add(tempChapterContent)
            }
        }

        return BookModel(book, chapters, chapterContents)
    }

    private fun getChildTableElement(element: Element): Element? {
        var tableElement: Element? = null
        var nextSibling = element.nextElementSibling()
        while (nextSibling != null) {
            if (nextSibling.tagName() == "table") {
                tableElement = nextSibling
                break
            }
            nextSibling = nextSibling.nextElementSibling()
        }
        return tableElement
    }

    private fun parseAndStoreChapterHtml(chapterElement: Element, currentPath: String): ChapterContentModel {
        val tempHeadings: MutableList<Heading> = mutableListOf()
        val tempImages: MutableList<Image> = mutableListOf()
        val tempParagraphs: MutableList<Paragraph> = mutableListOf()

        val elements = chapterElement.children()
        var chapterContentCount = 0
        for (element in elements) {
            // Any subheadings in the chapter
            if (element.tagName() == "h3") {
                chapterContentCount++
                val heading = element.text()
                val tempHeading = Heading(0, 0, chapterContentCount, heading)
                tempHeadings.add(tempHeading)
            }
            // Any paragraphs in the chapter
            if (element.tagName() == "p") {
                chapterContentCount++
                val paragraph = element.text()
                val tempParagraph = Paragraph(0, 0, chapterContentCount, paragraph)
                tempParagraphs.add(tempParagraph)
            }
            // Any images in the chapter
            if (element.tagName() == "div" && element.hasClass("fig")) {
                chapterContentCount++
                val image = element.selectFirst("img")
                if (image != null) {
                    val relativePath = image.attr("src")
                    val absolutePath = "${currentPath}/${relativePath}"

                    // Sometimes, the image's alt is just [Illustration] which is not clear.
                    // In that case, it's better to use the caption's text if it exists.
                    val caption = element.selectFirst("p.caption")
                    val alt = caption?.text() ?: image.attr("alt")

                    val tempImage = Image(0, 0, chapterContentCount, absolutePath, alt)
                    tempImages.add(tempImage)
                }
            }
        }

        return ChapterContentModel(headings = tempHeadings, images = tempImages, paragraphs = tempParagraphs)
    }
}