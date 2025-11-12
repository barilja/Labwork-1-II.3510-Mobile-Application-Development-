package com.tumme.scrudstudents.ui.teacher

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.R
import com.tumme.scrudstudents.ui.components.TableHeader
import com.tumme.scrudstudents.ui.teaches.TeachViewModel
import com.tumme.scrudstudents.ui.subscribe.SubscribeViewModel
import com.tumme.scrudstudents.ui.student.StudentListViewModel
import com.tumme.scrudstudents.ui.course.CourseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherStudentList(
    teacherId: Int,
    // ViewModels are injected using Hilt so UI does not instantiate them manually
    teachViewModel: TeachViewModel = hiltViewModel(),
    subscribeViewModel: SubscribeViewModel = hiltViewModel(),
    studentViewModel: StudentListViewModel = hiltViewModel(),
    courseViewModel: CourseViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    // UI collects data from StateFlows exposed by ViewModels
    val teaches by teachViewModel.teaches.collectAsState()
    val subscribes by subscribeViewModel.subscribes.collectAsState()
    val students by studentViewModel.students.collectAsState()
    val courses by courseViewModel.courses.collectAsState()

    // Filter only the courses taught by this teacher (logic stays in UI because it is derived data)
    val teacherCourseIds = teaches.filter { it.teacherId == teacherId }.map { it.courseId }

    // Filter subscriptions only related to those courses
    val subscribedStudents = subscribes.filter { it.courseId in teacherCourseIds }

    // Join student + course + subscription data to display a combined row in UI
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
                title = { Text(stringResource(R.string.your_students)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to Home")
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
                cells = listOf(stringResource(R.string.student_id_teacher_list),
                    stringResource(R.string.full_name_teacher_list),
                    stringResource(R.string.course_id_teacher_list),
                    stringResource(R.string.course_name_teacher_list)
                ),
                weights = listOf(0.2f, 0.4f, 0.2f, 0.4f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // UI displays result from composed, joined state, without querying repository directly
            if (studentCourseDetails.isEmpty()) {
                Text(stringResource(R.string.no_students_enrolled_in_your_courses_student_list))
            } else {
                LazyColumn {
                    items(studentCourseDetails) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .horizontalScroll(scrollState)
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

// Simple UI model used to present combined data to the Composable.
// This prevents Composable from needing raw DB entities or business objects.
data class StudentCourseItem(
    val studentId: Int,
    val fullName: String,
    val courseId: Int,
    val courseName: String
)
