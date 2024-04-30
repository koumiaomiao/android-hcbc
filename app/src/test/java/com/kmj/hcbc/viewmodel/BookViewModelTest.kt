package com.kmj.hcbc.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.kmj.hcbc.model.Book
import com.kmj.hcbc.repository.BookRepository
import com.kmj.hcbc.repository.remote.network.BffErrorResponse
import com.kmj.hcbc.repository.remote.network.Resource
import com.kmj.hcbc.utils.Action
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.internal.notify
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.net.HttpURLConnection
import java.net.SocketTimeoutException

@OptIn(ExperimentalCoroutinesApi::class)
class BookViewModelTest {
    @get:Rule
    internal val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK(relaxed = true)
    lateinit var repository: BookRepository

    private lateinit var viewModel: BookViewModel
    private lateinit var books: List<Book>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = BookViewModel(repository)
        books = listOf(Book("1", "title", "author", "1998", "123456"))
    }

    @Test
    fun should_fetch_all_books_when_books_api_repository_response_success() {
        coEvery {
            repository.fetchAllBooks()
        } returns Resource.success(books)

        viewModel.fetchAllBooks()

        viewModel.booksLiveData.observeForTesting {
            assert(viewModel.booksLiveData.value == books)
        }
    }

    @Test
    fun should_fetch_all_books_return_error_when_book_api_repository_return_server_error_resource() {
        coEvery {
            repository.fetchAllBooks()
        } returns Resource.error(
            BffErrorResponse(HttpURLConnection.HTTP_NOT_IMPLEMENTED.toString(), "服务器错误", null),
            null
        )

        viewModel.fetchAllBooks()

        viewModel.actionLiveData.observeForTesting {
            assert((viewModel.actionLiveData.value != null))
        }
    }

    @Test
    fun should_fetch_all_books_return_error_when_fetch_all_books_repository_return_network_error_resource() {
        coEvery {
            repository.fetchAllBooks()
        } returns Resource.error(null, SocketTimeoutException())

        viewModel.fetchAllBooks()

        viewModel.actionLiveData.observeForTesting {
            Assert.assertTrue(viewModel.actionLiveData.value != null)
        }
    }

    private fun <T> LiveData<T>.observeForTesting(block: () -> Unit) {
        val observer = Observer<T> { }
        try {
            observeForever(observer)
            block()
        } finally {
            removeObserver(observer)
        }
    }

    internal class MainDispatcherRule(
        private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
    ) : TestWatcher() {

        override fun starting(description: Description) {
            Dispatchers.setMain(testDispatcher)
        }

        override fun finished(description: Description) {
            Dispatchers.resetMain()
        }
    }
}
