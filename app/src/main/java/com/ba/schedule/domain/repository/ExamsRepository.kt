package com.ba.schedule.domain.repository

import com.ba.schedule.domain.model.Exam
import kotlinx.coroutines.flow.Flow

interface ExamsRepository {

    fun getAll(): Flow<List<Exam>>

    suspend fun getById(id: Int): List<Exam>

    suspend fun add(exam: Exam)

    suspend fun remove(exam: Exam)
}