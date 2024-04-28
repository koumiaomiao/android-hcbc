package com.kmj.hcbc.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmj.hcbc.model.Book
import com.kmj.hcbc.repository.BookRepository
import com.kmj.hcbc.repository.remote.network.State
import com.kmj.hcbc.utils.ActionLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
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

    init {
        fetchAllBooks()
    }

    fun fetchAllBooks() {
        viewModelScope.launch {
            val resource = repository.fetchAllBooks()
            when (resource.status) {
                State.SUCCESS -> _booksLiveData.value = resource.data
                else -> {
                    if (resource.throwable is IOException) {
                        actionLiveData.sendAction(Action.NetworkError)
                    } else {
                        actionLiveData.sendAction(Action.FetchDataError(resource.errorBody?.code))
                    }
                }
            }
        }
    }

    fun createBook(book: Book) {
        viewModelScope.launch {
            val resource = repository.createBook(book)
            when (resource.status) {
                State.SUCCESS -> _latestBook.value = resource.data
                else -> {
                    if (resource.throwable is IOException) {
                        actionLiveData.sendAction(Action.NetworkError)
                    } else {
                        actionLiveData.sendAction(Action.FetchDataError(resource.errorBody?.code))
                    }
                }
            }
        }
    }

    fun updateBook(id: String, book: Book) {
        viewModelScope.launch {
            val resource = repository.updateBook(id, book)
            when (resource.status) {
                State.SUCCESS -> _latestBook.value = resource.data
                else -> {
                    if (resource.throwable is IOException) {
                        actionLiveData.sendAction(Action.NetworkError)
                    } else {
                        actionLiveData.sendAction(Action.FetchDataError(resource.errorBody?.code))
                    }
                }
            }
        }
    }

    fun deleteBookById(id: String) {
        viewModelScope.launch {
            val resource = repository.deleteBookById(id)
            when (resource.status) {
                State.SUCCESS -> repository.fetchAllBooks()
                else -> {
                    if (resource.throwable is IOException) {
                        actionLiveData.sendAction(Action.NetworkError)
                    } else {
                        actionLiveData.sendAction(Action.FetchDataError(resource.errorBody?.code))
                    }
                }
            }
        }
    }

    fun findBookById(id: String) {
        viewModelScope.launch {
            val resource = repository.fetchBookById(id)
            when (resource.status) {
                State.SUCCESS -> _foundBook.value = resource.data
                else -> {
                    if (resource.throwable is IOException) {
                        actionLiveData.sendAction(Action.NetworkError)
                    } else {
                        actionLiveData.sendAction(Action.FetchDataError(resource.errorBody?.code))
                    }
                }
            }
        }
    }

    sealed class Action {
        class FetchDataError(val code: String?) : Action()
        object NetworkError : Action()
    }
}
