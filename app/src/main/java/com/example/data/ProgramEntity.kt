package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_programs")
data class ProgramEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val code: String,
    val timestamp: Long = System.currentTimeMillis()
)
