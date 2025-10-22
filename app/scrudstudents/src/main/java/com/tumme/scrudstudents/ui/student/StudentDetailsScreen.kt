package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.StudentEntity
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
//Defines a composable screen to display the details of a single student.
@Composable
fun StudentDetailScreen(
    // The unique ID of the student to display, passed via navigation.
    studentId: Int,
    // Injects the ViewModel to fetch student data.
    viewModel: StudentListViewModel = hiltViewModel(),
    // A lambda to be called to navigate back to the previous screen.
    onBack: ()->Unit = {}
) {
    // A formatter to display the date of birth in a user-friendly format.
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    // A mutable state to hold the fetched StudentEntity. It's nullable and starts as null.
    var student by remember { mutableStateOf<StudentEntity?>(null) }

    // A side-effect that runs once when the `studentId` changes.
    // It launches a coroutine to fetch the student details from the ViewModel.
    LaunchedEffect(studentId) {
        // Calls the suspend function on the ViewModel to find the student by their ID and updates the state.
        student = viewModel.findStudent(studentId)
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Student details") }, navigationIcon = {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
        })
    }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            // Checks if the student data is still being loaded.
            if (student == null) {
                // Shows a simple loading text while fetching data.
                Text("Loading...")
            } else {
                // Once the student data is loaded, display the details.
                Text("ID: ${student!!.idStudent}")
                Text("Name: ${student!!.firstName} ${student!!.lastName}")
                Text("DOB: ${sdf.format(student!!.dateOfBirth)}")
                Text("Gender: ${student!!.gender.value}")
            }
        }
    }
}
