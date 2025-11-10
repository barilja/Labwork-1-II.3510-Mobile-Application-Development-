package com.tumme.scrudstudents.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.AdminEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
//the constructor is injected by Hilt.
class AdminViewModel @Inject constructor(
    //receives an instance of SCRUDRepository, which is the single source of truth for student data.
    private val repo: SCRUDRepository
) : ViewModel() { // Inherits from ViewModel, making it lifecycle-aware.

    fun insertAdmin(admin: AdminEntity) = viewModelScope.launch {
        // Calls the repository's suspend function to insert the student.
        repo.insertAdmin(admin)
    }
    fun deleteAdmin(admin:AdminEntity)=viewModelScope.launch {
        repo.deleteAdmin(admin)
    }
    suspend fun getAdminById(id:Int)=repo.getAdminById(id)
    suspend fun getAdminByEmail(email:String)=repo.getAdminByEmail(email)
}