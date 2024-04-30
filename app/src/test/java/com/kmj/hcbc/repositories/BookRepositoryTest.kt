package com.kmj.hcbc.repositories

import com.kmj.hcbc.model.Book
import com.kmj.hcbc.repository.BookRepository
import com.kmj.hcbc.repository.remote.BookRepositoryImpl
import com.kmj.hcbc.repository.remote.api.BookApiService
import com.kmj.hcbc.repository.remote.network.BffErrorResponse
import retrofit2.Response.error
import com.kmj.hcbc.repository.remote.network.State
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.buffer
import okio.source
import org.json.JSONObject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException

class BookRepositoryTest {

    @MockK
    lateinit var bookApiService: BookApiService

    @MockK(relaxed = true)
    lateinit var response: Response

    private lateinit var repository: BookRepository
    private lateinit var books: List<Book>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = BookRepositoryImpl(bookApiService)
        books = listOf(Book("1", "title", "author", "1998", "123456"))
    }

    @Test
    fun should_fetch_all_books_return_data_when_fetch_all_books_service_response_success() =
        runBlocking {
            coEvery {
                bookApiService.fetchAllBooks()
            } returns books

            val resource = repository.fetchAllBooks()

            assertEquals(State.SUCCESS, resource.status)
            Assert.assertEquals(true, resource.data?.isNotEmpty())
        }

    @Test
    fun should_fetch_all_books_return_error_when_fetch_all_books_service_response_failure() =
        runBlocking {
            val errorBody = generateErrorBody("error")
            coEvery {
                repository.fetchAllBooks()
            } throws (HttpException(error<BffErrorResponse>(errorBody.toResponseBody(), response)))

            val resource = repository.fetchAllBooks()

            assertEquals(State.ERROR, resource.status)
            Assert.assertEquals(
                HttpURLConnection.HTTP_NOT_IMPLEMENTED.toString(),
                resource.errorBody?.code
            )
            Assert.assertEquals("服务器错误", resource.errorBody?.message)
        }

    @Test
    fun should_fetch_all_books_repository_return_error_resource_when_fetch_all_books_request_throw_exception() =
        runBlocking {
            coEvery {
                bookApiService.fetchAllBooks()
            } throws (SocketTimeoutException())

            val resource = repository.fetchAllBooks()
            assertEquals(State.ERROR, resource.status)
            Assert.assertTrue(resource.throwable is SocketTimeoutException)
        }

    private fun generateErrorBody(key: String): String {
        val inputStream = javaClass.getResourceAsStream("/response/mockResponse.json")
        val source = inputStream!!.source().buffer()
        val jsonBody = JSONObject(source.readString(Charsets.UTF_8))
        return jsonBody.getJSONObject(key).toString()
    }
}
