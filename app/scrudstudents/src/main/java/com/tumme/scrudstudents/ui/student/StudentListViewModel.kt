package com.tumme.scrudstudents.ui.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
//the constructor is injected by Hilt.
class StudentListViewModel @Inject constructor(
    //receives an instance of SCRUDRepository, which is the single source of truth for student data.
    private val repo: SCRUDRepository
) : ViewModel() { // Inherits from ViewModel, making it lifecycle-aware.

    // A private StateFlow that holds the current list of students.
    // It's initialized by calling the repository's `getAllStudents()` function, which returns a Flow.
    // `stateIn` converts this cold Flow into a hot StateFlow, which keeps the last emitted value and shares it among collectors.
    // `viewModelScope`: The flow is collected within the lifecycle of the ViewModel.
    // `SharingStarted.Lazily`: The upstream flow from the repository is started only when the first subscriber appears and is stopped when the last subscriber disappears.
    // `emptyList()`: The initial value of the StateFlow before the repository emits any data.
    private val _students: StateFlow<List<StudentEntity>> =
        repo.getAllStudents().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // A public, read-only version of the `_students` StateFlow.
    // This exposes the student list to the UI (like a Composable screen) to observe for changes.
    // The UI collects this flow to get updates automatically whenever the student data changes in the database.
    val students: StateFlow<List<StudentEntity>> = _students

    // A private MutableSharedFlow used to send one-time events (like snackbar messages) from the ViewModel to the UI.
    private val _events = MutableSharedFlow<String>()
    // A public, read-only version of the events flow for the UI to collect.
    val events = _events.asSharedFlow()

    /*
      Deletes a student from the database.
      This function launches a new coroutine in the `viewModelScope` to perform the database operation on a background thread.
      @param student The StudentEntity object to be deleted.
     */
    fun deleteStudent(student: StudentEntity) = viewModelScope.launch {
        // Calls the repository's suspend function to delete the student.
        repo.deleteStudent(student)
        // Emits an event to the UI to confirm that the student has been deleted.
        _events.emit("Student deleted")
    }

    /*
      Inserts a student into the database.
      This also launches a coroutine in the `viewModelScope` for the background database operation.
      @param student The StudentEntity object to be inserted.
     */
    fun insertStudent(student: StudentEntity) = viewModelScope.launch {
        // Calls the repository's suspend function to insert the student.
        repo.insertStudent(student)
        // Emits an event to the UI to confirm that the student has been inserted.
        _events.emit("Student inserted")
    }

    /*
      Finds a single student by their ID.
      This is a suspend function, meaning it needs to be called from a coroutine.
      It directly calls the repository's method to fetch the student.
      @param id The unique integer ID of the student to find.
      @return The found StudentEntity, or null if no student with that ID exists.
     */
    suspend fun findStudent(id: Int) = repo.getStudentById(id)

}

//the data flow goes from the UI to this file (studentlistviewmodel), to the scrudrepository, to the dao and finally to the local db.
