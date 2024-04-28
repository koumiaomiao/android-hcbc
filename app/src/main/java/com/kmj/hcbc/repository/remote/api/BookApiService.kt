package com.kmj.hcbc.repository.remote.api

import com.kmj.hcbc.model.Book
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BookApiService {
    @GET("/api/books")
    suspend fun fetchAllBooks(): List<Book>?

    @GET("/api/books/{id}")
    suspend fun fetchBookById(@Path("id") id: String): Book?

    @POST("/api/books")
    suspend fun createBook(@Body book: Book): Book?

    @DELETE("/api/books/{id}")
    suspend fun deleteBookById(@Path("id") id: String): Unit?

    @PUT("/api/books/{id}")
    suspend fun updateBook(@Path("id") id: String, @Body book: Book): Book?
}
