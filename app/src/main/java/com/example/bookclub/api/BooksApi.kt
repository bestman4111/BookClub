package com.example.bookclub.api

import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApi {
    @GET("search.json?subject=fiction&limit=100")
    suspend fun getBooks(): BookResponse

    @GET("search.json?limit=20")
    suspend fun searchBooks(@Query("q") query: String): BookResponse
}