package com.tumme.scrudstudents.ui.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
//the constructor is injected by Hilt.
class CourseViewModel @Inject constructor(
    //receives an instance of SCRUDRepository, which is the single source of truth for course data.
    private val repo: SCRUDRepository
) : ViewModel() { // Inherits from ViewModel, making it lifecycle-aware.

    // A private StateFlow that holds the current list of courses.
    // It's initialized by calling the repository's `getAllCourses()` function, which returns a Flow.
    // `stateIn` converts this cold Flow into a hot StateFlow, which keeps the last emitted value and shares it among collectors.
    // `viewModelScope`: The flow is collected within the lifecycle of the ViewModel.
    // `SharingStarted.WhileSubscribed()`: The upstream flow from the repository is started when the first subscriber appears and is stopped when the last subscriber disappears.
    // `emptyList()`: The initial value of the StateFlow before the repository emits any data.
    private val _courses: StateFlow<List<CourseEntity>> =
        repo.getAllCourses().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    // A public, read-only version of the `_courses` StateFlow.
    // This exposes the course list to the UI (like a Composable screen) to observe for changes.
    // The UI collects this flow to get updates automatically whenever the course data changes in the database.
    val courses: StateFlow<List<CourseEntity>> = _courses

    // A private MutableSharedFlow used to send one-time events (like snackbar messages) from the ViewModel to the UI.
    private val _events= MutableSharedFlow<String>()
    // A public, read-only version of the events flow for the UI to collect.
    val events= _events.asSharedFlow()

    /*
      Deletes a course from the database.
      This function launches a new coroutine in the `viewModelScope` to perform the database operation on a background thread.
      @param course The CourseEntity object to be deleted.
     */
    fun deleteCourse(course: CourseEntity) = viewModelScope.launch {
        // Calls the repository's suspend function to delete the course.
        repo.deleteCourse(course)
        // Emits an event to the UI to confirm that the course has been deleted.
        _events.emit("Course deleted")
    }

    /*
      Inserts a course into the database.
      This also launches a coroutine in the `viewModelScope` for the background database operation.
      @param course The CourseEntity object to be inserted.
     */
    fun insertCourse(course: CourseEntity) = viewModelScope.launch {
        // Calls the repository's suspend function to insert the course.
        repo.insertCourse(course)
        // Emits an event to the UI to confirm that the course has been inserted.
        _events.emit("Course inserted")
    }

    /*
      Finds a single course by its ID.
      This is a suspend function, meaning it needs to be called from a coroutine.
      It directly calls the repository's method to fetch the course.
      @param id The unique integer ID of the course to find.
      @return The found CourseEntity, or null if no course with that ID exists.
     */
    suspend fun getCourseById(id: Int) = repo.getCourseById(id)

}
