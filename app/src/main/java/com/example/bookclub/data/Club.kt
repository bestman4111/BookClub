package com.example.bookclub.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clubs")
data class Club(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val bookTitle: String,
    val isPrivate: Boolean,
    val password: String
)