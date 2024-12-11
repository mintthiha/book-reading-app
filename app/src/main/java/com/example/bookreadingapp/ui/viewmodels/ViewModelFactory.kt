package com.example.bookreadingapp.ui.viewmodels

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val application: Application, private val sharedPreferences: SharedPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(BookProcessingViewModel::class.java) -> {
                BookProcessingViewModel(application) as T
            }
            modelClass.isAssignableFrom(BookViewModel::class.java) -> {
                BookViewModel(application, sharedPreferences) as T
            }
            modelClass.isAssignableFrom(BookReadingAppViewModel::class.java) -> {
                BookReadingAppViewModel(application, sharedPreferences) as T
            }
            // To add another view model, inspire from the following example
            // modelClass.isAssignableFrom(AnotherViewModel::class.java) -> {
            //    AnotherViewModel() as T
            //}
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}