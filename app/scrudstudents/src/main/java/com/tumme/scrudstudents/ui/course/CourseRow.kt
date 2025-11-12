package com.tumme.scrudstudents.ui.course

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tumme.scrudstudents.data.local.model.CourseEntity

// A composable function responsible for displaying a single course's data in a row.
@Composable
fun CourseRow(
    // The course data to be displayed.
    course: CourseEntity,
    // Lambda to be executed when the edit action is triggered.
    onEdit: () -> Unit,
    // Lambda to be executed when the delete action is triggered.
    onDelete: () -> Unit,
    // Lambda to be executed when the view action is triggered.
    onView: () -> Unit,
    // Lambda to be executed when the share action is triggered.
    onShare: () -> Unit
) {
    // Creates a state for managing horizontal scrolling.
    val scrollState = rememberScrollState()
    // A Row arranges its children horizontally.
    Row(
        modifier = Modifier
            .fillMaxWidth() // The row will take up the full available width.
            .horizontalScroll(scrollState) // Allows the row to be scrolled horizontally if the content overflows.
            .padding(8.dp), // Adds padding around the row.
        verticalAlignment = Alignment.CenterVertically // Aligns children vertically to the center.
    ) {
        // Displays the course ID.
        Text(text = course.idCourse.toString(), modifier = Modifier.width(100.dp))
        // Displays the course name.
        Text(text = course.nameCourse, modifier = Modifier.width(100.dp))
        // Displays the ECTS credits for the course.
        Text(text = course.ectsCourse.toString(), modifier = Modifier.width(100.dp))
        // Displays the level of the course.
        Text(text = course.levelCourse.name, modifier = Modifier.width(100.dp))

        // A nested Row for the action icons.
        Row(modifier = Modifier.width(200.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            // An icon button that triggers the `onView` lambda when clicked.
            IconButton(onClick = onView) { Icon(Icons.Default.Info, contentDescription = "View") }
            // An icon button that triggers the `onEdit` lambda when clicked.
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Edit") }
            // An icon button that triggers the `onDelete` lambda when clicked.
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Delete") }
            // An icon button that triggers the `onShare` lambda when clicked.
            IconButton(onClick = onShare) { Icon(Icons.Default.Share, contentDescription = "Share") }
        }
    }
    // Displays a horizontal line to separate this row from the next.
    HorizontalDivider()
}
