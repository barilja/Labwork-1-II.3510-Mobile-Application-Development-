package com.tumme.scrudstudents.domain.usecase.student

import com.tumme.scrudstudents.data.repository.SCRUDRepository

// Defines a use case for getting all students.
class GetStudentsUseCase(private val repo: SCRUDRepository) {
    /*
      The `invoke` operator allows the class instance to be called as a function.
      This function directly calls the repository method to get all students.
      It returns a Flow, which allows the UI to observe the student list for changes in real-time.
      @return A Flow that emits a list of StudentEntity objects.
     */
    operator fun invoke() = repo.getAllStudents()
}
