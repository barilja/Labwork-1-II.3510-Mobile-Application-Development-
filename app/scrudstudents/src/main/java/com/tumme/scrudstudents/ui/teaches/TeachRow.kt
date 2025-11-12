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
    teach: TeachEntity,            // Represents the Teach entity being displayed
    onDelete: () -> Unit,         // Callback to request deletion (UI does not delete directly)
    viewModel: TeachViewModel = hiltViewModel() // ViewModel provided via Hilt
) {
    val scrollState = rememberScrollState()

    // UI state to store the fetched Course: Compose will recompose when this changes
    var course by remember { mutableStateOf<CourseEntity?>(null) }

    // Side-effect: fetch the course when this TeachRow is shown or when 'teach' changes
    // This respects MVVM: the UI requests data, but the ViewModel retrieves it.
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
        // Display fetched course information.
        // If still loading, fallback strings avoid UI crashes.
        Text(
            text = course?.idCourse.toString(),
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = course?.nameCourse ?: "Loading...",
            modifier = Modifier.width(150.dp)
        )
        Text(
            text = course?.ectsCourse.toString(),
            modifier = Modifier.width(150.dp)
        )
        Text(
            text = course?.levelCourse.toString(),
            modifier = Modifier.width(150.dp)
        )

        // UI triggers deletion via callback; ViewModel handles logic and data source
        Row(
            modifier = Modifier.width(200.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }

    HorizontalDivider()
}
