package com.example.bookclub.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)

    @Query("SELECT * FROM messages WHERE clubName = :clubName ORDER BY id ASC")
    fun getMessagesForClub(clubName: String): Flow<List<Message>>
}