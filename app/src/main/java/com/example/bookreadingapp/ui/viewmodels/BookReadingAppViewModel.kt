package com.example.bookreadingapp.ui.viewmodels

import android.app.Application
import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.bookreadingapp.R

/**
 * ViewModel for managing the state and operations of the Book Reading App.
 * This includes handling reading mode and search queries, as well as managing the download URLs.
 *
 * @param application The application context to access resources like the book URLs.
 * @param sharedPreferences The SharedPreferences for saving and retrieving app state (such as downloaded URLs).
 */
class BookReadingAppViewModel(
    private val application: Application, private val sharedPreferences: SharedPreferences
) : ViewModel() {
    // --- Properties ---
    /**
     * Mutable state indicating whether the app is in reading mode.
     * Default is true.
     */
    private val _isReadingMode : MutableState<Boolean> = mutableStateOf(false)
    val isReadingMode : Boolean
        get() = _isReadingMode.value

    /**
     * Mutable state for storing the current search query.
     * Default is an empty string.
     */
    private val _query : MutableState<String> = mutableStateOf("")
    val query : String
        get() = _query.value

    /**
     * Mutable state for storing the last saved search query.
     * Default is an empty string.
     */
    private val _savedQuery : MutableState<String> = mutableStateOf("")
    val savedQuery : String
        get() = _savedQuery.value

    /**
     * Mutable state for storing the urls availlable for download
     */
    private val _urlsAvailableForDownload = mutableStateListOf<String>()
    val urlsAvailableForDownload: List<String> get() = _urlsAvailableForDownload

    // Mutable state list to track URLs to download
    private val _urlsToDownload = mutableStateListOf<String>()
    val urlsToDownload: List<String> get() = _urlsToDownload

    init {
        // Initialize urls to download if necessary
        val bookUrls = application.resources.getStringArray(R.array.book_urls)
        initializeUrls(bookUrls.toList())
    }

    // --- Methods ---
    /**
     * Toggles the reading mode between true and false.
     * Reading mode can be used to hide the navigation bar..
     */
    fun toggleIsReadingMode() {
        // Add validation that is reading mode is only for reading screen?
        _isReadingMode.value = !(_isReadingMode.value)
    }

    /**
     * Updates the current search query.
     *
     * @param newQuery The new search query to be stored.
     */
    fun updateQuery(newQuery : String) {
        _query.value = newQuery
        // In the future, this function will also update the list to display in the search?
    }

    /**
     * Updates the saved search query with the current query.
     */
    fun updateSavedQuery() {
        _savedQuery.value = _query.value
    }

    /**
     * Adds a URL to the download queue, if it's available for download.
     *
     * @param url The URL to be downloaded.
     */
    fun downloadUrl(url: String) {
        if (_urlsAvailableForDownload.contains(url)) {
            _urlsAvailableForDownload.remove(url)
            _urlsToDownload.add(url)
            sharedPreferences.edit()
                // It is very unlikely a url will contain a ###
                .putString("urls_available_for_download", _urlsAvailableForDownload.joinToString("###"))
                .apply()
        }
    }

    /**
     * Removes a URL from the download queue, indicating that it has been downloaded.
     *
     * @param url The URL to be marked as downloaded.
     */
    fun downloadedUrl(url: String) {
        if (_urlsToDownload.contains(url)) {
            _urlsToDownload.remove(url)
        }
    }

    /**
     * Initializes the available and downloading URLs.
     * This is done once, to load the URLs into the app.
     *
     * @param initialUrls A list of initial URLs fetched from resources.
     */
    private fun initializeUrls(initialUrls: List<String>) {
        val areInitialBooksDownloaded = sharedPreferences.getBoolean("are_initial_books_downloaded", false)
        if (!areInitialBooksDownloaded) {
            _urlsAvailableForDownload.addAll(initialUrls)
            _urlsToDownload.addAll(_urlsAvailableForDownload.take(3))
            _urlsAvailableForDownload.removeAll(_urlsToDownload)
            sharedPreferences.edit()
                // It is very unlikely a url will contain a ###
                .putString("urls_available_for_download", _urlsAvailableForDownload.joinToString("###"))
                .putBoolean("are_initial_books_downloaded", true)
                .apply()
        } else {
            val urlsAvailableForDownload = sharedPreferences.getString("urls_available_for_download", "")
            // It is very unlikely a url will contain a ###
            val urlsList = urlsAvailableForDownload?.split("###")?.filter { it.isNotEmpty() }
            if (urlsList != null) {
                _urlsAvailableForDownload.addAll(urlsList)
            }
        }
    }
}