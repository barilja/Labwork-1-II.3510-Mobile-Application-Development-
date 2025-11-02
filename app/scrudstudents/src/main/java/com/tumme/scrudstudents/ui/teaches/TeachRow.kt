package com.tumme.scrudstudents.ui.teaches

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
import com.tumme.scrudstudents.data.local.model.TeachEntity
import com.tumme.scrudstudents.data.local.model.CourseEntity

@Composable
fun TeachRow(
    teach: TeachEntity,
    onDelete: () -> Unit,
    viewModel: TeachViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()

    // Local states for course and student names
    var course by remember { mutableStateOf<CourseEntity?>(null) }

    // Fetch data asynchronously when the subscribe changes
    LaunchedEffect(teach) {
        course = viewModel.getCourseById(teach.courseId)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = course?.idCourse.toString() ?: "Loading...",
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = course?.nameCourse ?: "Loading...",
            modifier = Modifier.width(150.dp)
        )
        Text(
            text = course?.ectsCourse.toString() ?: "Loading...",
            modifier = Modifier.width(150.dp)
        )
        Text(
            text = course?.levelCourse.toString() ?: "Loading...",
            modifier = Modifier.width(150.dp)
        )
        Row(
            modifier = Modifier.width(200.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Delete") }
        }
    }

    Divider()
}
