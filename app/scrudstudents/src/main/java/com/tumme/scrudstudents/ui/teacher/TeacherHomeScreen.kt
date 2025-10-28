package com.tumme.scrudstudents.ui.teacher

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.ui.teacher.TeacherViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherHomeScreen(
    teacherId: Int,
    // Injects the ViewModel to fetch course data.
    viewModel: TeacherViewModel = hiltViewModel()
    // A lambda to be called to navigate back to the previous screen.
) {
    // A mutable state to hold the fetched TeacherEntity. It's nullable and starts as null.
    var teacher by remember { mutableStateOf<TeacherEntity?>(null) }

    // A side-effect that runs once when the `courseId` changes.
    // It launches a coroutine to fetch the course details from the ViewModel.
    LaunchedEffect(teacherId) {
        // Calls the suspend function on the ViewModel to find the courses by their ID and updates the state.
        teacher = viewModel.findTeacher(teacherId)
    }
    Scaffold(topBar = {
        TopAppBar(title = { Text("Teacher home page") })
    }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            // Checks if the course data is still being loaded.
            if (teacher == null) {
                // Shows a simple loading text while fetching data.
                Text("Loading...")
            } else {
                // Once the student data is loaded, display the details.
                Text("ID: ${teacher!!.idTeacher}")
                Text("Name: ${teacher!!.firstName}+${teacher!!.lastName}")
                Text("ECTS: ${teacher!!.dateOfBirth}")
                Text("Level: ${teacher!!.email}")
            }
        }
    }
}