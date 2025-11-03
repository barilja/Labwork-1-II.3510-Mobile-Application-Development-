package com.tumme.scrudstudents.ui.subscribe

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.SubscribeEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribeFormScreen(
    studentId:Int,
    viewModel: SubscribeViewModel = hiltViewModel(),
    onSaved: () -> Unit = {}
) {
    // Collect the lists of courses and students from the ViewModel.
    val courses by viewModel.courses.collectAsState()
    val context = LocalContext.current

    // State for managing whether the dropdown menus are expanded or not.
    var courseMenuExpanded by remember { mutableStateOf(false) }

    // State to hold the currently selected course and student from the dropdowns.
    var selectedCourse by remember { mutableStateOf<CourseEntity?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // Course Selection Dropdown
        ExposedDropdownMenuBox(
            expanded = courseMenuExpanded,
            onExpandedChange = { courseMenuExpanded = !courseMenuExpanded }
        ) {
            TextField(
                value = selectedCourse?.nameCourse ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Course") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = courseMenuExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = courseMenuExpanded,
                onDismissRequest = { courseMenuExpanded = false }
            ) {
                courses.forEach { course ->
                    DropdownMenuItem(
                        text = { Text(course.nameCourse) },
                        onClick = {
                            selectedCourse = course
                            courseMenuExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Save Button
        Button(onClick = {
            if (selectedCourse == null) {
                Toast.makeText(context, "Please select a course.", Toast.LENGTH_SHORT).show()
            } else {
                val subscribe = SubscribeEntity(
                    studentId = studentId,
                    courseId = selectedCourse!!.idCourse,
                    score = 0.toFloat()
                )
                viewModel.insertSubscription(subscribe)
                onSaved()
            }
        }) {
            Text("Save Subscription")
        }
    }
}
