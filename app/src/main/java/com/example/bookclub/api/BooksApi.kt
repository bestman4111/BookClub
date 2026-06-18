package com.example.bookclub.api

import retrofit2.http.GET

interface BooksApi {
    @GET("search.json?subject=fiction&limit=15")
    suspend fun getBooks(): BookResponse
}