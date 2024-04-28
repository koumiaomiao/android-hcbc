package com.kmj.hcbc.repository

import com.kmj.hcbc.model.Book
import com.kmj.hcbc.repository.remote.network.Resource

interface BookApiRepository {
    suspend fun fetchAllBooks(): Resource<List<Book>?>
}
