package com.tumme.scrudstudents.ui.course

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.LevelCourse

// Opt-in to use experimental Material 3 APIs.
@OptIn(ExperimentalMaterial3Api::class)
// Defines a Composable screen for editing an existing course.
@Composable
fun CourseEditScreen(
    // The unique ID of the course to be edited, passed via navigation.
    courseId: Int,
    // Injects the CourseViewModel using Hilt for data operations.
    viewModel: CourseViewModel = hiltViewModel(),
    // A callback function to be invoked when the changes are saved, typically to navigate back.
    onSave: () -> Unit = {}
) {
    // A mutable state to hold the full CourseEntity object once it's fetched from the database.
    var course by remember { mutableStateOf<CourseEntity?>(null) }

    // A side-effect that runs once the screen is composed or if `courseId` changes.
    // It launches a coroutine to fetch the specific course's details from the ViewModel.
    LaunchedEffect(courseId) {
        course = viewModel.getCourseById(courseId)
    }

    // Scaffold provides the basic Material Design layout structure.
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Course") },
                // A navigation icon that acts as a back button, triggering the onSave (navigate back) callback.
                navigationIcon = {
                    IconButton(onClick = onSave) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        // Conditional content: shows a loading indicator until the course data is available.
        if (course == null) {
            // A centered loading spinner to indicate that data is being fetched.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // When the course data is loaded, these mutable states are created to hold the editable field values.
            var name by remember { mutableStateOf(course!!.nameCourse) }
            var ectsText by remember { mutableStateOf(course!!.ectsCourse.toString()) }
            var level by remember { mutableStateOf(course!!.levelCourse) }

            // Remembers a scroll state for the horizontal row of level buttons.
            val scrollState = rememberScrollState()

            // The main form content, arranged vertically.
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                // A text field for editing the course name.
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Course name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                // A text field for editing the ECTS credits.
                TextField(
                    value = ectsText,
                    onValueChange = { ectsText = it },
                    label = { Text("ECTS") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                // A label for the level selector.
                Text("Level:", style = MaterialTheme.typography.titleMedium)
                // A horizontally scrollable row for the level selection buttons.
                Row(
                    modifier = Modifier
                        .horizontalScroll(scrollState)
                        .padding(top = 8.dp)
                ) {
                    // Creates a button for each entry in the LevelCourse enum.
                    LevelCourse.entries.forEach { courseLevel ->
                        val selected = level == courseLevel
                        Button(
                            onClick = { level = courseLevel },
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .height(40.dp),
                            // Changes the button's color to highlight the currently selected level.
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selected)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Text(courseLevel.name)
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // The main save button for the form.
                Button(
                    onClick = {
                        // Parses the ECTS text to a Float, defaulting to 0f if invalid.
                        val ects = ectsText.toFloatOrNull() ?: 0f
                        // Creates a new CourseEntity by copying the original and applying the edited values.
                        val updatedCourse = course!!.copy(
                            nameCourse = name,
                            ectsCourse = ects,
                            levelCourse = level
                        )
                        // Calls the ViewModel to insert the course. Room's `insert` will automatically update it because the primary key is the same.
                        viewModel.insertCourse(updatedCourse)
                        // Triggers the callback to navigate back to the previous screen.
                        onSave()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Save")
                }
            }
        }
    }
}
