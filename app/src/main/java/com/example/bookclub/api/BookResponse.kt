package com.example.bookclub.api

import com.google.gson.annotations.SerializedName

data class BookResponse(
    val docs: List<BookApiItem>
)

data class BookApiItem(
    val title: String,
    @SerializedName("author_name")
    val authorName: List<String>?,
    @SerializedName("first_publish_year")
    val firstPublishYear: Int?
)