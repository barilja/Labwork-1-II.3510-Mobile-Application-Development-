package com.tumme.scrudstudents.ui.subscribe

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.local.model.TeachEntity
import com.tumme.scrudstudents.ui.course.CourseViewModel
import com.tumme.scrudstudents.ui.teacher.TeacherViewModel
import com.tumme.scrudstudents.ui.teaches.TeachViewModel

@Composable
fun SubscribeRow(
    subscribe: SubscribeEntity,
    onDelete: () -> Unit,
    subscribeViewModel: SubscribeViewModel = hiltViewModel(),
    courseViewModel: CourseViewModel = hiltViewModel(),
    teachViewModel: TeachViewModel = hiltViewModel(),
    teacherViewModel: TeacherViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()

    var course by remember { mutableStateOf<CourseEntity?>(null) }
    var teach by remember { mutableStateOf<TeachEntity?>(null) }
    var professor by remember { mutableStateOf<TeacherEntity?>(null) }

    // Fetch related data asynchronously
    LaunchedEffect(subscribe) {
        // 1️⃣ Get the course
        course = courseViewModel.getCourseById(subscribe.courseId)

        // 2️⃣ Get the teach relationship for that course
        course?.let { c ->
            teach = teachViewModel.getTeachByCourse(c.idCourse)
        }

        // 3️⃣ Get the teacher based on the teach entry
        teach?.let { t ->
            professor = teacherViewModel.findTeacher(t.teacherId)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = course?.idCourse?.toString() ?: "Loading...",
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = course?.nameCourse ?: "Loading...",
            modifier = Modifier.width(150.dp)
        )
        Text(
            text = course?.ectsCourse?.toString() ?: "-",
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = professor?.lastName ?: "Unknown",
            modifier = Modifier.width(150.dp)
        )
        Text(
            text = subscribe.score?.toString() ?: "-",
            modifier = Modifier.width(80.dp)
        )
        Row(
            modifier = Modifier.width(100.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }

    HorizontalDivider()
}
