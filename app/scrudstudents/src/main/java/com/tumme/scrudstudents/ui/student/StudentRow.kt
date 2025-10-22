package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
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
import com.tumme.scrudstudents.data.local.model.StudentEntity
import java.text.SimpleDateFormat
import java.util.Locale

// A composable function responsible for displaying a single student's data in a row.
@Composable
fun StudentRow(
    // The student data to be displayed.
    student: StudentEntity,
    // Lambda to be executed when the edit action is triggered.
    onEdit: ()->Unit,
    // Lambda to be executed when the delete action is triggered.
    onDelete: ()->Unit,
    // Lambda to be executed when the view action is triggered.
    onView: ()->Unit,
    // Lambda to be executed when the share action is triggered.
    onShare: ()->Unit
) {
    // A formatter to display the date in a specific string format.
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    // A Row arranges its children horizontally.
    // Create a horizontal scroll state
    val scrollState = rememberScrollState()
    Row(modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(scrollState)
        .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Displays the formatted date of birth
        Text(text = student.dateOfBirth.let { dateFormat.format(it) }, modifier = Modifier.width(120.dp))
        // Displays the student's last name
        Text(text = student.lastName, modifier = Modifier.width(150.dp))
        // Displays the student's first name
        Text(text = student.firstName, modifier = Modifier.width(150.dp))
        // Displays the student's gender
        Text(text = student.gender.value, modifier = Modifier.width(80.dp))
        // A nested Row for the action icons
        Row(modifier = Modifier.width(200.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            // An icon button that triggers the `onView` lambda when clicked.
            IconButton(onClick = onView) { Icon(Icons.Default.Info, contentDescription="View") }
            // An icon button that triggers the `onEdit` lambda when clicked.
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription="Edit") }
            // An icon button that triggers the `onDelete` lambda when clicked.
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription="Delete") }
            // An icon button that triggers the `onShare` lambda when clicked.
            IconButton(onClick = onShare) { Icon(Icons.Default.Share, contentDescription="Share") }
        }
    }
    // Displays a horizontal line to separate this row from the next.
    Divider()
}
