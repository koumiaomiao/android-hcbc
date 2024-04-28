package com.kmj.hcbc.repository.remote.api

import com.kmj.hcbc.model.Book
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BookApiService {
    @GET("/api/books")
    suspend fun fetchAllBooks(): List<Book>?

    @POST("/api/books")
    suspend fun createBook(@Body book: Book): Book?
}
