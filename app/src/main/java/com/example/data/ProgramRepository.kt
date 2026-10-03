package com.example.data

import kotlinx.coroutines.flow.Flow

class ProgramRepository(private val dao: ProgramDao) {
    val allPrograms: Flow<List<ProgramEntity>> = dao.getAllPrograms()

    suspend fun getById(id: Long): ProgramEntity? = dao.getProgramById(id)

    suspend fun save(program: ProgramEntity): Long = dao.insertProgram(program)

    suspend fun delete(id: Long) = dao.deleteProgram(id)
}
