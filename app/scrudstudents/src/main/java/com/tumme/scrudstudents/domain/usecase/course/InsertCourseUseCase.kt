package com.tumme.scrudstudents.domain.usecase.course

import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository

// This class encapsulates the logic for a single action: inserting a course.
class InsertCourseUseCase(private val repo: SCRUDRepository) {
    /*
      The `invoke` operator allows this class to be called like a function.
      It's a suspend function, ensuring that the database insertion,
      is performed asynchronously without blocking the main thread.
      @param course The CourseEntity object to be inserted into the database.
     */
    suspend operator fun invoke(course: CourseEntity) = repo.insertCourse(course)
}