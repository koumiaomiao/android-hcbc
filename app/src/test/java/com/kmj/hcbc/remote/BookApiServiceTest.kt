package com.kmj.hcbc.remote

import com.kmj.hcbc.repository.remote.api.BookApiService
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.json.JSONObject
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class BookApiServiceTest {

    private val mockWebServer = MockWebServer()

    private lateinit var bookApiService: BookApiService

    @Before
    fun setUp() {
        bookApiService = createBookApiService()
    }

    @Test
    fun should_get_all_books_when_book_server_response_success() =
        runBlocking {
            configMockServer("books", HttpURLConnection.HTTP_OK)

            val response = bookApiService.fetchAllBooks()

            Assert.assertTrue(response?.isNotEmpty() ?: false)
        }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private fun configMockServer(key: String, responseCode: Int) {
        val inputStream = javaClass.getResourceAsStream("/response/mockResponse.json")
        val source = inputStream!!.source().buffer()
        val jsonBody = JSONObject(source.readString(Charsets.UTF_8))
        mockWebServer.enqueue(
            MockResponse()
                .setBody(jsonBody.getJSONArray(key).toString())
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
