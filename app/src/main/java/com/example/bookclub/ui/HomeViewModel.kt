package com.example.bookclub.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookclub.api.BookApiItem
import com.example.bookclub.api.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _books = MutableStateFlow<List<BookApiItem>>(emptyList())
    val books: StateFlow<List<BookApiItem>> = _books.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init{
        fetchBooks()
    }

    private fun fetchBooks() {
        viewModelScope.launch() {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getBooks()
                _books.value = response.docs
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Eroare: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}