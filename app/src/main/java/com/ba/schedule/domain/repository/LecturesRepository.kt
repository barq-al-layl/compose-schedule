package com.ba.schedule.domain.repository

import com.ba.schedule.domain.model.Lecture
import kotlinx.coroutines.flow.Flow

interface LecturesRepository {

    fun getAll(): Flow<List<Lecture>>

    suspend fun add(lecture: Lecture)

    suspend fun remove(lecture: Lecture)
}