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
    private val repo: SCRUDRepository
) : ViewModel() {

    // Exposes a state flow of all subscriptions from the repository.
    val teaches: StateFlow<List<TeachEntity>> =
        repo.getAllTeaches().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    // The UI will collect this flow to get the list of courses for the dropdown.
    val courses: StateFlow<List<CourseEntity>> =
        repo.getAllCourses().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    // Exposes a state flow of all students from the repository.
    val students: StateFlow<List<TeacherEntity>> =
        repo.getAllTeachers().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    // Inserts a new subscription record into the database.
    fun insertTeach(teach: TeachEntity) = viewModelScope.launch {
        repo.insertTeach(teach)
    }
    // Deletes a subscription record from the database.
    fun deleteTeach(teach: TeachEntity) = viewModelScope.launch {
        repo.deleteTeach(teach)
    }

    suspend fun getCourseById(id:Int)= repo.getCourseById(id)

    suspend fun getTeacherById(id:Int) = repo.getTeacherById(id)
    //retrieve the subscription by the idcourse
    suspend fun getSubscribe(id:Int)= repo.getTeachesByCourse(id)
}
