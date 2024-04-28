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

    val actionLiveData = ActionLiveData<Action>()

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

    sealed class Action {
        class FetchDataError(val code: String?) : Action()
        object NetworkError : Action()
    }
}
