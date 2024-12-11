package com.example.bookreadingapp

import com.example.bookreadingapp.data.repository.FileRepository
import com.example.bookreadingapp.ui.viewmodels.BookReadingAppViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

class BookReadingAppViewModelTests {
    private lateinit var viewModel: BookReadingAppViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        // Initialize the ViewModel with a mock repository and injected test dispatcher
        viewModel = BookReadingAppViewModel()
    }

    @Test
    fun toggleIsReadingMode_changesIsReadingModeToOppositeBoolean() {
        // Arrange
        val initialValue = viewModel.isReadingMode

        // Act
        viewModel.toggleIsReadingMode()

        // Assert
        assertEquals(!initialValue, viewModel.isReadingMode)
    }

    @Test
    fun toggleIsReadingModeTwice_changesIsReadingModeToSameBoolean() {
        // Arrange
        val initialValue = viewModel.isReadingMode

        // Act
        viewModel.toggleIsReadingMode()
        viewModel.toggleIsReadingMode()

        // Assert
        assertEquals(initialValue, viewModel.isReadingMode)
    }

    @Test
    fun updateQuery_updatesQueryAppropriately() {
        // Arrange

        // Act
        viewModel.updateQuery("Lorem")

        // Assert
        assertEquals("Lorem", viewModel.query)
    }
}