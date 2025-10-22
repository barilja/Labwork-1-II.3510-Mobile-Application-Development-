package com.tumme.scrudstudents.domain.usecase.course

import com.tumme.scrudstudents.data.repository.SCRUDRepository

class GetCourseUseCase (private val repo: SCRUDRepository){
    /*
      The `invoke` operator allows the class instance to be called as a function.
      This function directly calls the repository method to get all courses.
      It returns a Flow, which allows the UI to observe the course list for changes in real-time.
      @return A Flow that emits a list of CourseEntity objects.
     */
    operator fun invoke() = repo.getAllCourses()
}