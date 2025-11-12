package com.tumme.scrudstudents.ui.teaches

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.R
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.TeachEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeachFormScreen(
    teacherId: Int,
    viewModel: TeachViewModel = hiltViewModel(),   // ViewModel obtained from Hilt for state + business logic
    onSaved: () -> Unit = {}                      // Callback triggered when saving is completed
) {
    // UI observes the list of courses from ViewModel (StateFlow -> Compose state)
    val courses by viewModel.courses.collectAsState()

    val context = LocalContext.current

    // UI states - kept locally because they only affect UI interaction, not shared data
    var courseMenuExpanded by remember { mutableStateOf(false) }
    var selectedCourse by remember { mutableStateOf<CourseEntity?>(null) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Dropdown displays available courses fetched from Repository via ViewModel
        ExposedDropdownMenuBox(
            expanded = courseMenuExpanded,
            onExpandedChange = { courseMenuExpanded = !courseMenuExpanded }
        ) {
            TextField(
                value = selectedCourse?.nameCourse ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.select_course_teacher)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = courseMenuExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = courseMenuExpanded,
                onDismissRequest = { courseMenuExpanded = false }
            ) {
                courses.forEach { course ->
                    DropdownMenuItem(
                        text = { Text(course.nameCourse) },
                        onClick = {
                            selectedCourse = course       // UI remembers user's selection
                            courseMenuExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Save action triggers ViewModel -> Repository -> Database write operation
        Button(onClick = {
            if (selectedCourse == null) {
                Toast.makeText(context,
                    context.getString(R.string.please_select_a_course_teacher), Toast.LENGTH_SHORT).show()
            } else {
                // Entity prepared based on UI selections
                val teach = TeachEntity(
                    teacherId = teacherId,
                    courseId = selectedCourse!!.idCourse,
                )

                // ViewModel performs insertion (UI does not speak to DB directly)
                viewModel.insertTeach(teach)

                // Notifies navigation layer to go back / close form
                onSaved()
            }
        }) {
            Text(stringResource(R.string.save_teach))
        }
    }
}
