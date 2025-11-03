package com.tumme.scrudstudents.ui.teacher

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.ui.components.TableHeader
import com.tumme.scrudstudents.ui.teaches.TeachViewModel
import com.tumme.scrudstudents.ui.subscribe.SubscribeViewModel
import com.tumme.scrudstudents.ui.student.StudentListViewModel
import com.tumme.scrudstudents.ui.course.CourseViewModel  // 👈 to get course names

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherStudentList(
    teacherId: Int,
    teachViewModel: TeachViewModel = hiltViewModel(),
    subscribeViewModel: SubscribeViewModel = hiltViewModel(),
    studentViewModel: StudentListViewModel = hiltViewModel(),
    courseViewModel: CourseViewModel = hiltViewModel(), // 👈 added
    onNavigateBack: () -> Unit = {}
) {
    val teaches by teachViewModel.teaches.collectAsState()
    val subscribes by subscribeViewModel.subscribes.collectAsState()
    val students by studentViewModel.students.collectAsState()
    val courses by courseViewModel.courses.collectAsState()

    // 🧠 1. Filter courses taught by this teacher
    val teacherCourseIds = teaches.filter { it.teacherId == teacherId }.map { it.courseId }

    // 🧩 2. Filter subscriptions to those courses
    val subscribedStudents = subscribes.filter { it.courseId in teacherCourseIds }

    // 🧍 3. Join subscription + student + course info
    val studentCourseDetails = subscribedStudents.mapNotNull { sub ->
        val student = students.find { it.idStudent == sub.studentId }
        val course = courses.find { it.idCourse == sub.courseId }
        if (student != null && course != null) {
            StudentCourseItem(
                studentId = student.idStudent,
                fullName = "${student.firstName} ${student.lastName}",
                courseId = course.idCourse,
                courseName = course.nameCourse
            )
        } else null
    }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Students") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back to Home")
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
            TableHeader(
                cells = listOf("Student ID", "Full Name", "Course ID", "Course Name"),
                weights = listOf(0.2f, 0.4f, 0.2f, 0.4f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (studentCourseDetails.isEmpty()) {
                Text("No students enrolled in your courses.")
            } else {
                LazyColumn {
                    items(studentCourseDetails) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .horizontalScroll(scrollState) // 👈 enable horizontal scrolling
                        ) {
                            Text(item.studentId.toString(), modifier = Modifier.weight(0.2f))
                            Text(item.fullName, modifier = Modifier.weight(0.4f))
                            Text(item.courseId.toString(), modifier = Modifier.weight(0.2f))
                            Text(item.courseName, modifier = Modifier.weight(0.4f))
                        }
                    }
                }
            }
        }
    }
}

data class StudentCourseItem(
    val studentId: Int,
    val fullName: String,
    val courseId: Int,
    val courseName: String
)
