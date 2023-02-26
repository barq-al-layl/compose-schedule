package com.ba.schedule.di

import com.ba.schedule.data.repository.DefaultCoursesRepository
import com.ba.schedule.data.repository.DefaultExamsRepository
import com.ba.schedule.data.repository.DefaultLecturesRepository
import com.ba.schedule.domain.repository.CoursesRepository
import com.ba.schedule.domain.repository.ExamsRepository
import com.ba.schedule.domain.repository.LecturesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindCoursesRepository(
        repository: DefaultCoursesRepository,
    ): CoursesRepository

    @Binds
    abstract fun bindLecturesRepository(
        repository: DefaultLecturesRepository,
    ): LecturesRepository

    @Binds
    abstract fun bindExamsRepository(
        repository: DefaultExamsRepository,
    ): ExamsRepository

}