package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgramDao {
    @Query("SELECT * FROM saved_programs ORDER BY timestamp DESC")
    fun getAllPrograms(): Flow<List<ProgramEntity>>

    @Query("SELECT * FROM saved_programs WHERE id = :id")
    suspend fun getProgramById(id: Long): ProgramEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgram(program: ProgramEntity): Long

    @Query("DELETE FROM saved_programs WHERE id = :id")
    suspend fun deleteProgram(id: Long)
}
