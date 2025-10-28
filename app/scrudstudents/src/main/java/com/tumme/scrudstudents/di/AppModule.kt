package com.tumme.scrudstudents.di

import android.content.Context
import androidx.room.Room
import com.tumme.scrudstudents.data.local.AppDatabase
import com.tumme.scrudstudents.data.local.dao.*
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "scrud-db")
            .fallbackToDestructiveMigration() // ⚠️ This resets the DB if schema changes
            .build()

    @Provides fun provideStudentDao(db: AppDatabase): StudentDao = db.studentDao()
    @Provides fun provideCourseDao(db: AppDatabase): CourseDao = db.courseDao()
    @Provides fun provideSubscribeDao(db: AppDatabase): SubscribeDao = db.subscribeDao()
    @Provides fun provideTeacherDao(db: AppDatabase): TeacherDao = db.teacherDao()
    @Provides fun provideTeachDao(db: AppDatabase): TeachDao = db.teachDao()

    @Provides
    @Singleton
    fun provideRepository(
        studentDao: StudentDao,
        courseDao: CourseDao,
        subscribeDao: SubscribeDao,
        teacherDao: TeacherDao,
        teachDao: TeachDao
    ): SCRUDRepository = SCRUDRepository(
        studentDao,
        courseDao,
        subscribeDao,
        teacherDao,
        teachDao
    )
}
