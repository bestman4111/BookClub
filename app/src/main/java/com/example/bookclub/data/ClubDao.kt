package com.example.bookclub.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ClubDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClub(club: Club): Long

    @Query("SELECT * FROM clubs ORDER BY id DESC")
    fun getAllClubs(): Flow<List<Club>>
}