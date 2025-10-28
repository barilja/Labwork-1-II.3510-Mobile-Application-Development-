package com.tumme.scrudstudents.ui.course

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.ui.course.CourseViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
//Defines a composable screen to display the details of a single course.
@Composable
fun CourseDetailScreen(
    // The unique ID of the course to display, passed via navigation.
    courseId: Int,
    // Injects the ViewModel to fetch course data.
    viewModel: CourseViewModel = hiltViewModel(),
    // A lambda to be called to navigate back to the previous screen.
    onBack: ()->Unit = {}
) {
    // A mutable state to hold the fetched CourseEntity. It's nullable and starts as null.
    var course by remember { mutableStateOf<CourseEntity?>(null) }

    // A side-effect that runs once when the `courseId` changes.
    // It launches a coroutine to fetch the course details from the ViewModel.
    LaunchedEffect(courseId) {
        // Calls the suspend function on the ViewModel to find the courses by their ID and updates the state.
        course = viewModel.getCourseById(courseId)
    }
    Scaffold(topBar = {
        TopAppBar(title = { Text("Course details") }, navigationIcon = {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
        })
    }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            // Checks if the course data is still being loaded.
            if (course == null) {
                // Shows a simple loading text while fetching data.
                Text("Loading...")
            } else {
                // Once the student data is loaded, display the details.
                Text("ID: ${course!!.idCourse}")
                Text("Name: ${course!!.nameCourse}")
                Text("ECTS: ${course!!.ectsCourse}")
                Text("Level: ${course!!.levelCourse.name}")
            }
        }
    }
}