package com.tumme.scrudstudents.domain.usecase.student

import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository

// This class encapsulates the logic for a single action: inserting a student.
class InsertStudentUseCase(private val repo: SCRUDRepository) {
    /*
      The `invoke` operator allows this class to be called like a function.
      It's a suspend function, ensuring that the database insertion,
      is performed asynchronously without blocking the main thread.
      @param student The StudentEntity object to be inserted into the database.
     */
    suspend operator fun invoke(student: StudentEntity) = repo.insertStudent(student)
}
