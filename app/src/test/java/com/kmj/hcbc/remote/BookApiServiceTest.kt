package com.kmj.hcbc.remote

import com.google.gson.Gson
import com.kmj.hcbc.model.Book
import com.kmj.hcbc.repository.remote.api.BookApiService
import com.kmj.hcbc.repository.remote.network.BffErrorResponse
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import okhttp3.Response
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import okio.buffer
import okio.source
import org.json.JSONObject
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import java.net.NoRouteToHostException
import java.net.Socket
import java.net.SocketTimeoutException

class BookApiServiceTest {

    private val mockWebServer = MockWebServer()

    private lateinit var bookApiService: BookApiService

    @Before
    fun setUp() {
        bookApiService = createBookApiService()
    }

    @Test
    fun should_fetch_all_books_when_book_server_response_success() =
        runBlocking {
            configMockServer("books", HttpURLConnection.HTTP_OK)

            val response = bookApiService.fetchAllBooks()

            Assert.assertTrue(response?.isNotEmpty() ?: false)
        }

    @Test
    fun should_fetch_all_books_request_error_when_book_server_response_failure() {
        configMockServer("error", HttpURLConnection.HTTP_NOT_IMPLEMENTED)

        val exception = Assert.assertThrows(HttpException::class.java) {
            runBlocking {
                bookApiService.fetchAllBooks()
            }
        }
        val response = exception.response()?.errorBody()?.charStream()?.use {
            Gson().fromJson(it, BffErrorResponse::class.java)
        }

        Assert.assertEquals(HttpURLConnection.HTTP_NOT_IMPLEMENTED, exception.code())
        Assert.assertEquals("501", response?.code)
        Assert.assertEquals("服务器错误", response?.message)
    }

    @Test(expected = SocketTimeoutException::class)
    fun should_fetch_all_books_request_throw_connect_timeout_exception_when_network_error() {
        mockWebServer.enqueue(MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE))

        runBlocking {
            bookApiService.fetchAllBooks()
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private fun configMockServer(key: String, responseCode: Int) {
        val inputStream = javaClass.getResourceAsStream("/response/mockResponse.json")
        val source = inputStream!!.source().buffer()
        val jsonBody = JSONObject(source.readString(Charsets.UTF_8))
        val body = if (key == "books") {
            jsonBody.getJSONArray(key).toString()
        } else {
            jsonBody.getJSONObject(key).toString()
        }
        mockWebServer.enqueue(
            MockResponse()
                .setBody(body)
                .setResponseCode(responseCode)
                .addHeader("Content-Type", "application/json")
        )
    }

    private fun createBookApiService() = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(BookApiService::class.java)
}
