package com.tumme.scrudstudents.domain.usecase.course

import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository

// This class defines a single, specific task: deleting a student.
class DeleteCourseUseCase(private val repo: SCRUDRepository) {
    /*
      The `invoke` operator allows the class instance to be called as if it were a function.
      It's a suspend function to ensure that the underlying database operation
      does not block the main thread.
      @param course The CourseEntity object to be deleted.
     */
    suspend operator fun invoke(course: CourseEntity) = repo.deleteCourse(course)
}
