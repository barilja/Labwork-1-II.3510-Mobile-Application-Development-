package com.tumme.scrudstudents.ui.teacher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
//the constructor is injected by Hilt.
class TeacherViewModel @Inject constructor(
    //receives an instance of SCRUDRepository, which is the single source of truth for teacher data.
    private val repo: SCRUDRepository
) : ViewModel() { // Inherits from ViewModel, making it lifecycle-aware.

    // A private StateFlow that holds the current list of teachers.
    // It's initialized by calling the repository's `getAllTeachers()` function, which returns a Flow.
    // `stateIn` converts this cold Flow into a hot StateFlow, which keeps the last emitted value and shares it among collectors.
    // `viewModelScope`: The flow is collected within the lifecycle of the ViewModel.
    // `SharingStarted.Lazily`: The upstream flow from the repository is started only when the first subscriber appears and is stopped when the last subscriber disappears.
    // `emptyList()`: The initial value of the StateFlow before the repository emits any data.
    private val _teachers: StateFlow<List<TeacherEntity>> =
        repo.getAllTeachers().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // A public, read-only version of the `_teachers` StateFlow.
    // This exposes the teacher list to the UI (like a Composable screen) to observe for changes.
    // The UI collects this flow to get updates automatically whenever the teacher data changes in the database.
    val teachers: StateFlow<List<TeacherEntity>> = _teachers

    // A private MutableSharedFlow used to send one-time events (like snackbar messages) from the ViewModel to the UI.
    private val _events = MutableSharedFlow<String>()
    // A public, read-only version of the events flow for the UI to collect.
    val events = _events.asSharedFlow()

    /*
      Deletes a teacher from the database.
      This function launches a new coroutine in the `viewModelScope` to perform the database operation on a background thread.
      @param teacher The TeacherEntity object to be deleted.
     */
    fun deleteTeacher(teacher: TeacherEntity) = viewModelScope.launch {
        // Calls the repository's suspend function to delete the teacher.
        repo.deleteTeacher(teacher)
        // Emits an event to the UI to confirm that the teacher has been deleted.
        _events.emit("Teacher deleted")
    }

    /*
      Inserts a teacher into the database.
      This also launches a coroutine in the `viewModelScope` for the background database operation.
      @param teacher The TeacherEntity object to be inserted.
     */
    fun insertTeacher(teacher: TeacherEntity) = viewModelScope.launch {
        // Calls the repository's suspend function to insert the student.
        repo.insertTeacher(teacher)
        // Emits an event to the UI to confirm that the student has been inserted.
        _events.emit("Teacher inserted")
    }

    /*
      Finds a single teacher by their ID.
      This is a suspend function, meaning it needs to be called from a coroutine.
      It directly calls the repository's method to fetch the teacher.
      @param id The unique integer ID of the teacher to find.
      @return The found TeacherEntity, or null if no teacher with that ID exists.
     */
    suspend fun findTeacher(id: Int) = repo.getTeacherById(id)

}

//the data flow goes from the UI to this file (teacherviewmodel), to the scrudrepository, to the dao and finally to the local db.
