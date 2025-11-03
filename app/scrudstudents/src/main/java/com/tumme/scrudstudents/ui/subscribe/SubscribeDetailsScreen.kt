package com.tumme.scrudstudents.ui.subscribe

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.StudentEntity
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribeDetailScreen(
    courseId: Int,
    studentId: Int,
    score:Float,
    viewModel: SubscribeViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {//information about the course and the student useful for the subscription details
    var course by remember { mutableStateOf<CourseEntity?>(null) }
    var student by remember { mutableStateOf<StudentEntity?>(null) }
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    LaunchedEffect(courseId, studentId) {
        course = viewModel.getCourseById(courseId)
        student = viewModel.getStudentById(studentId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Subscription details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (course == null || student == null) {
                Text("Loading...")
            } else {
                Text("Course ID: ${course!!.idCourse}")
                Text("Course Name: ${course!!.nameCourse}")
                Text("ECTS: ${course!!.ectsCourse}")
                Text("Level: ${course!!.levelCourse.name}")
                Text("Score: $score")//score showed next to the course attributes
                Spacer(modifier = Modifier.height(8.dp))
                Text("Student ID: ${student!!.idStudent}")
                Text("Student Name: ${student!!.firstName} ${student!!.lastName}")
                Text("Date of Birth: ${sdf.format(student!!.dateOfBirth)}")
                Text("Gender: ${student!!.gender.value}")
            }
        }
    }
}
