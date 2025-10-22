package com.tumme.scrudstudents.ui.subscribe

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.SubscribeEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribeEditScreen(
    courseId: Int,
    studentId: Int,
    score: Float,
    viewModel: SubscribeViewModel = hiltViewModel(),
    onSave: () -> Unit = {}
) {//vars to retrieve both student and course related to the specific subscription
    var course by remember { mutableStateOf<CourseEntity?>(null) }
    var student by remember { mutableStateOf<StudentEntity?>(null) }
    var newScore by remember { mutableStateOf(score.toString()) }

    // Load data from ViewModel
    LaunchedEffect(courseId, studentId) {
        course = viewModel.getCourseById(courseId)
        student = viewModel.getStudentById(studentId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Subscription") },
                navigationIcon = {
                    IconButton(onClick = onSave) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (course == null || student == null) {
            // Loading indicator
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Course name (read-only)
                OutlinedTextField(
                    value = course!!.nameCourse,
                    onValueChange = {},
                    label = { Text("Course Name") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )

                // Student name (read-only)
                OutlinedTextField(
                    value = "${student!!.firstName} ${student!!.lastName}",
                    onValueChange = {},
                    label = { Text("Student Name") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )

                // Editable score field
                OutlinedTextField(
                    value = newScore,
                    onValueChange = { newScore = it },
                    label = { Text("Score") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        val updatedScore = newScore.toFloatOrNull() ?: score
                        val updatedSubscribe = SubscribeEntity(
                            courseId = courseId,
                            studentId = studentId,
                            score = updatedScore
                        )
                        viewModel.insertSubscription(updatedSubscribe)
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
