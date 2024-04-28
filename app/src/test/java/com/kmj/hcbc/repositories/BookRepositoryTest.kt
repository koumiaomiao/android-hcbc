package com.kmj.hcbc.repositories

import com.kmj.hcbc.model.Book
import com.kmj.hcbc.repository.BookRepository
import com.kmj.hcbc.repository.remote.BookRepositoryImpl
import com.kmj.hcbc.repository.remote.api.BookApiService
import com.kmj.hcbc.repository.remote.network.State
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class BookRepositoryTest {

    @MockK
    lateinit var bookApiService: BookApiService

    private lateinit var repository: BookRepository
    private lateinit var books: List<Book>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = BookRepositoryImpl(bookApiService)
        books = listOf(Book("1", "title", "author", "1998", "123456"))
    }

    @Test
    fun should_book_repository_return_books_when_fetch_all_books_service_response_success() =
        runBlocking {
            coEvery {
                bookApiService.fetchAllBooks()
            } returns books

            val resource = repository.fetchAllBooks()

            assertEquals(State.SUCCESS, resource.status)
            Assert.assertEquals(true, resource.data?.isNotEmpty())
        }
}
