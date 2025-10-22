package com.tumme.scrudstudents.ui.subscribe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscribeViewModel @Inject constructor(
    private val repo: SCRUDRepository
) : ViewModel() {

    // Exposes a state flow of all subscriptions from the repository.
    val subscribes: StateFlow<List<SubscribeEntity>> =
        repo.getAllSubscribes().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    // The UI will collect this flow to get the list of courses for the dropdown.
    val courses: StateFlow<List<CourseEntity>> =
        repo.getAllCourses().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    // Exposes a state flow of all students from the repository.
    val students: StateFlow<List<StudentEntity>> =
        repo.getAllStudents().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    // Inserts a new subscription record into the database.
    fun insertSubscription(subscribe: SubscribeEntity) = viewModelScope.launch {
        repo.insertSubscribe(subscribe)
    }
    // Deletes a subscription record from the database.
    fun deleteSubscription(subscribe: SubscribeEntity) = viewModelScope.launch {
        repo.deleteSubscribe(subscribe)
    }

    suspend fun getCourseById(id:Int)= repo.getCourseById(id)

    suspend fun getStudentById(id:Int) = repo.getStudentById(id)
    //retrieve the subscription by the idcourse
    suspend fun getSubscribe(id:Int)= repo.getSubscribesByCourse(id)
}
