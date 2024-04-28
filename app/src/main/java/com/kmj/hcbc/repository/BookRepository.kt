package com.kmj.hcbc.repository

import com.kmj.hcbc.model.Book
import com.kmj.hcbc.repository.remote.network.Resource

interface BookRepository {
    suspend fun fetchAllBooks(): Resource<List<Book>?>

    suspend fun createBook(book: Book): Resource<Book?>
}
