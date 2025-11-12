package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.R
import com.tumme.scrudstudents.data.local.model.StudentEntity
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentHomeScreen(
    studentId: Int, // ID of the student passed from navigation
    viewModel: StudentListViewModel = hiltViewModel(), // Injected ViewModel via Hilt (MVVM pattern)
    onNavigateToCourseList: (Int) -> Unit = { _ -> }, // Callback to navigate to the student's course list
    onNavigateToFinalGrades: (Int) -> Unit = { _ -> }, // Callback to navigate to final grades screen
    onLogout: () -> Unit = {} // Callback to log the student out
) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Format date of birth for display
    var student by remember { mutableStateOf<StudentEntity?>(null) } // Holds current student info

    // Fetch student details when the screen loads or studentId changes
    LaunchedEffect(studentId) {
        student = viewModel.findStudent(studentId) // Call ViewModel to get student entity
    }

    // Scaffold provides a basic page structure with top bar and content
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.student_home_page)) }, // Simple app bar title
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize() // Column fills entire screen
                .padding(padding) // Respect Scaffold padding (status bar / navigation bar)
                .padding(16.dp) // Inner padding for content
        ) {
            // Show loading text while student data is being fetched
            if (student == null) {
                Text(stringResource(R.string.loading))
            } else {
                // Display student information once loaded
                Text(stringResource(R.string.id_student_home, student!!.idStudent)) // Student ID
                Text(
                    stringResource(
                        R.string.name_student_home,
                        student!!.firstName,
                        student!!.lastName
                    )) // Full Name
                Text(
                    stringResource(
                        R.string.date_of_birth_student_home,
                        student!!.dateOfBirth.let { dateFormat.format(it) })) // DOB formatted
                Text(stringResource(R.string.email_student_home, student!!.email)) // Email
                Text(stringResource(R.string.level_student_home, student!!.levelCourse)) // Current level/course of the student

                Spacer(modifier = Modifier.height(24.dp)) // Space before buttons

                // Button to navigate to the student's course list
                Button(onClick = { onNavigateToCourseList(student!!.idStudent) }) {
                    Text(stringResource(R.string.go_to_course_list))
                }

                Spacer(modifier = Modifier.height(12.dp)) // Small spacing between buttons

                // Button to navigate to the student's final grades page
                Button(onClick = { onNavigateToFinalGrades(student!!.idStudent) }) {
                    Text(stringResource(R.string.go_to_final_grades_page))
                }

                Spacer(modifier = Modifier.height(12.dp)) // Space before logout button

                // Logout button
                Button(onClick = { onLogout() }) {
                    Text(stringResource(R.string.logout_student_home))
                    Alignment.Center // Ensures button content is centered
                }
            }
        }
    }
}
