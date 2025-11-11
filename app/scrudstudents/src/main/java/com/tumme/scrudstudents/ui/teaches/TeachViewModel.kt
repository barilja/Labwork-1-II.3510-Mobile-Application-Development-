package com.tumme.scrudstudents.ui.teaches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.local.model.TeachEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeachViewModel @Inject constructor(
    private val repo: SCRUDRepository  // Repository injected through Hilt (dependency inversion)
) : ViewModel() {

    // Collects list of TeachEntity from repository and exposes it as StateFlow.
    // UI observes this flow to display and react to database updates.
    val teaches: StateFlow<List<TeachEntity>> =
        repo.getAllTeaches().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    // Exposes list of courses. Used by UI for displaying or selecting courses related to each Teach.
    val courses: StateFlow<List<CourseEntity>> =
        repo.getAllCourses().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    // Exposes list of teachers. Used wherever teacher information is needed.
    val students: StateFlow<List<TeacherEntity>> =
        repo.getAllTeachers().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    // Inserts a TeachEntity into the database.
    // Runs inside viewModelScope to respect lifecycle and avoid leaks.
    fun insertTeach(teach: TeachEntity) = viewModelScope.launch {
        repo.insertTeach(teach)
    }

    // Requests deletion of a TeachEntity. UI does not access database directly.
    fun deleteTeach(teach: TeachEntity) = viewModelScope.launch {
        repo.deleteTeach(teach)
    }

    // Suspend functions used for fetching single related entities.
    // Called from UI inside a LaunchedEffect to avoid blocking the main thread.
    suspend fun getCourseById(id: Int) = repo.getCourseById(id)

    suspend fun getTeacherById(id: Int) = repo.getTeacherById(id)

    // Retrieves a list of teaches filtered by course id.
    suspend fun getSubscribe(id: Int) = repo.getTeachesByCourse(id)
    suspend fun getTeachByCourse(id:Int)=repo.getTeachByCourse(id)
}
