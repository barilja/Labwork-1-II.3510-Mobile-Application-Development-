package com.tumme.scrudstudents.ui.subscribe

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.SubscribeEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribeFormScreen(
    viewModel: SubscribeViewModel = hiltViewModel(),
    onSaved: () -> Unit = {}
) {
    // Collect the lists of courses and students from the ViewModel.
    val courses by viewModel.courses.collectAsState()
    val students by viewModel.students.collectAsState()
    val context = LocalContext.current

    // State for managing whether the dropdown menus are expanded or not.
    var courseMenuExpanded by remember { mutableStateOf(false) }
    var studentMenuExpanded by remember { mutableStateOf(false) }

    // State to hold the currently selected course and student from the dropdowns.
    var selectedCourse by remember { mutableStateOf<CourseEntity?>(null) }
    var selectedStudent by remember { mutableStateOf<StudentEntity?>(null) }

    // State for the score input field.
    var scoreText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // Course Selection Dropdown
        ExposedDropdownMenuBox(
            expanded = courseMenuExpanded,
            onExpandedChange = { courseMenuExpanded = !courseMenuExpanded }
        ) {
            TextField(
                value = selectedCourse?.nameCourse ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Course") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = courseMenuExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = courseMenuExpanded,
                onDismissRequest = { courseMenuExpanded = false }
            ) {
                courses.forEach { course ->
                    DropdownMenuItem(
                        text = { Text(course.nameCourse) },
                        onClick = {
                            selectedCourse = course
                            courseMenuExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        //Student Selection Dropdown
        ExposedDropdownMenuBox(
            expanded = studentMenuExpanded,
            onExpandedChange = { studentMenuExpanded = !studentMenuExpanded }
        ) {
            TextField(
                value = "${selectedStudent?.firstName ?: ""} ${selectedStudent?.lastName ?: ""}",
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Student") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = studentMenuExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = studentMenuExpanded,
                onDismissRequest = { studentMenuExpanded = false }
            ) {
                students.forEach { student ->
                    DropdownMenuItem(
                        text = { Text("${student.firstName} ${student.lastName}") },
                        onClick = {
                            selectedStudent = student
                            studentMenuExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Score Input
        TextField(
            value = scoreText,
            onValueChange = { scoreText = it },
            label = { Text("Score") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        // Save Button
        Button(onClick = {
            val score = scoreText.toFloatOrNull()
            if (selectedCourse == null || selectedStudent == null || score == null) {
                Toast.makeText(context, "Please select a student, a course, and enter a valid score.", Toast.LENGTH_SHORT).show()
            } else {
                val subscription = SubscribeEntity(
                    studentId = selectedStudent!!.idStudent,
                    courseId = selectedCourse!!.idCourse,
                    score = score
                )
                viewModel.insertSubscription(subscription)
                onSaved()
            }
        }) {
            Text("Save Subscription")
        }
    }
}
