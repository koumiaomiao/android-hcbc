package com.kmj.hcbc.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmj.hcbc.model.Book
import com.kmj.hcbc.repository.BookRepository
import com.kmj.hcbc.repository.remote.network.State
import com.kmj.hcbc.utils.Action
import com.kmj.hcbc.utils.ActionLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(private val repository: BookRepository) : ViewModel() {

    private val _booksLiveData = MutableLiveData<List<Book>?>()
    val booksLiveData = _booksLiveData

    private val _latestBook = MutableLiveData<Book?>()
    val latestBook = _latestBook

    private val _foundBook = MutableLiveData<Book?>()
    val foundBook = _foundBook

    val actionLiveData = ActionLiveData<Action>()
    var isLoading = MutableLiveData(false)

    init {
        fetchAllBooks()
    }

    fun fetchAllBooks() {
        isLoading.value = true
        viewModelScope.launch {
            val resource = repository.fetchAllBooks()
            isLoading.value = false
            when (resource.status) {
                State.SUCCESS -> _booksLiveData.value = resource.data
                else -> actionLiveData.sendAction(Action.FetchDataError)
            }
        }
    }

    fun createBook(book: Book) {
        isLoading.value = true
        viewModelScope.launch {
            val resource = repository.createBook(book)
            isLoading.value = false
            when (resource.status) {
                State.SUCCESS -> _latestBook.value = resource.data
                else -> actionLiveData.sendAction(Action.CreateDataError)
            }
        }
    }

    fun updateBook(id: String, book: Book) {
        isLoading.value = true
        viewModelScope.launch {
            val resource = repository.updateBook(id, book)
            isLoading.value = false
            when (resource.status) {
                State.SUCCESS -> _latestBook.value = resource.data
                else -> actionLiveData.sendAction(Action.UpdateDataError)
            }
        }
    }

    fun deleteBookById(id: String) {
        isLoading.value = true
        viewModelScope.launch {
            val resource = repository.deleteBookById(id)
            isLoading.value = false
            when (resource.status) {
                State.SUCCESS -> fetchAllBooks()
                else -> actionLiveData.sendAction(Action.DeleteDataError)
            }
        }
    }

    fun findBookById(id: String) {
        isLoading.value = true
        viewModelScope.launch {
            val resource = repository.fetchBookById(id)
            isLoading.value = false
            _foundBook.value = resource.data
            if (resource.status == State.ERROR) {
                actionLiveData.sendAction(Action.SearchDataError)
            }
        }
    }
}
