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
    studentId:Int, // Passed from navigation — UI knows which student is subscribing
    viewModel: SubscribeViewModel = hiltViewModel(), // ViewModel injected via Hilt (MVVM dependency injection)
    onSaved: () -> Unit = {} // Callback to navigate back or refresh list after saving
) {
    // ViewModel exposes list of courses as a StateFlow → collected here to update UI automatically
    val courses by viewModel.courses.collectAsState()

    val context = LocalContext.current // Only needed for showing Toast (UI concern)

    // State variables for the dropdown UI (not ViewModel, because these are temporary UI states)
    var courseMenuExpanded by remember { mutableStateOf(false) }
    var selectedCourse by remember { mutableStateOf<CourseEntity?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // UI dropdown for selecting a Course — UI reads ViewModel state but does NOT modify DB directly
        ExposedDropdownMenuBox(
            expanded = courseMenuExpanded,
            onExpandedChange = { courseMenuExpanded = !courseMenuExpanded }
        ) {
            TextField(
                value = selectedCourse?.nameCourse ?: "",
                onValueChange = {},
                readOnly = true, // UI is selection-driven, manual typing is disabled
                label = { Text("Select Course") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = courseMenuExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = courseMenuExpanded,
                onDismissRequest = { courseMenuExpanded = false }
            ) {
                // UI builds dropdown options from ViewModel-provided courses list
                courses.forEach { course ->
                    DropdownMenuItem(
                        text = { Text(course.nameCourse) },
                        onClick = {
                            selectedCourse = course // UI state update only
                            courseMenuExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // When saving, UI triggers ViewModel action → ViewModel calls Repository → Repository updates DB
        Button(onClick = {
            if (selectedCourse == null) {
                Toast.makeText(context, "Please select a course.", Toast.LENGTH_SHORT).show()
            } else {
                // UI prepares entity object (model layer object)
                val subscribe = SubscribeEntity(
                    studentId = studentId,
                    courseId = selectedCourse!!.idCourse,
                    score = 0.toFloat() // Default initial value
                )

                // MVVM core: UI → ViewModel → Repository → Local DB (Room)
                viewModel.insertSubscription(subscribe)

                // Notify caller (Screen) to navigate back or refresh
                onSaved()
            }
        }) {
            Text("Save Subscription")
        }
    }
}
