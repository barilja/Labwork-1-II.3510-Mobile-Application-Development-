package com.tumme.scrudstudents.ui.subscribe

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.StudentEntity

@Composable
fun SubscribeRow(
    subscribe: SubscribeEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onView: () -> Unit,
    onShare: () -> Unit,
    viewModel: SubscribeViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()

    // Local states for course and student names
    var course by remember { mutableStateOf<CourseEntity?>(null) }
    var student by remember { mutableStateOf<StudentEntity?>(null) }

    // Fetch data asynchronously when the subscribe changes
    LaunchedEffect(subscribe) {
        course = viewModel.getCourseById(subscribe.courseId)
        student = viewModel.getStudentById(subscribe.studentId)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = course?.nameCourse ?: "Loading...",
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = student?.lastName ?: "Loading...",
            modifier = Modifier.width(150.dp)
        )
        Text(
            text = subscribe.score.toString(),
            modifier = Modifier.width(80.dp)
        )
        Row(
            modifier = Modifier.width(200.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = onView) { Icon(Icons.Default.Info, contentDescription = "View") }
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Edit") }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Delete") }
            IconButton(onClick = onShare) { Icon(Icons.Default.Share, contentDescription = "Share") }
        }
    }

    Divider()
}
