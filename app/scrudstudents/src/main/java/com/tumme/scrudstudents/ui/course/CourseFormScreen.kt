package com.tumme.scrudstudents.ui.course

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.LevelCourse
import com.tumme.scrudstudents.data.local.model.CourseEntity

@Composable
fun CourseFormScreen(
    // Injects the CourseViewModel using Hilt for data operations.
    viewModel: CourseViewModel = hiltViewModel(),
    // A callback function to be invoked when the form is successfully saved.
    onSaved: () -> Unit = {}
) {
    // Creates a mutable state for the course's ID, initialized with a random number. 'remember' ensures the state survives recomposition.
    var id by remember { mutableStateOf((0..10000).random()) }
    // Creates a mutable state for the course name, initialized as an empty string.
    var name by remember { mutableStateOf("") }
    // Creates a mutable state for the ECTS credits as text, initialized to a default value.
    var ectsText by remember { mutableStateOf("5.0") }
    // Creates a mutable state for the course level, initialized to 'MS'.
    var level by remember { mutableStateOf(LevelCourse.MS) }

    // Creates a mutable state for holding a potential validation error message for the ECTS field.
    var ectsError by remember { mutableStateOf<String?>(null) } // For validation message

    // Creates and remembers a scroll state to allow the level selection row to be horizontally scrollable.
    val scrollState = rememberScrollState()

    // A Column composable that arranges its children vertically and fills the maximum available space with padding.
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // A text field for entering the course name. The 'name' state is updated on every value change.
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Course Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        // A text field for entering the ECTS credits. The 'ectsText' state is updated on every value change.
        TextField(
            value = ectsText,
            onValueChange = {
                ectsText = it
                ectsError = null // Clears the error message as the user types.
            },
            label = { Text("ECTS Credits") },
            isError = ectsError != null, // The field is marked as invalid if an error exists.
            modifier = Modifier.fillMaxWidth()
        )
        // Displays the validation error message below the text field if it exists.
        if (ectsError != null) {
            Text(
                text = ectsError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(8.dp))

        // A horizontally scrollable row for the course level selection buttons.
        Row(modifier = Modifier.horizontalScroll(scrollState)) {
            // Iterates through all available LevelCourse options to create a button for each.
            LevelCourse.entries.forEach { courseLevel ->
                // A button that, when clicked, updates the 'level' state to the selected course level.
                Button(
                    onClick = { level = courseLevel },
                    modifier = Modifier.padding(end = 8.dp),
                    // Changes the button's color to indicate whether it is the currently selected level.
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (level == courseLevel)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(courseLevel.name)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // The main save button for the form.
        Button(onClick = {
            // Tries to parse the ECTS text into a Float.
            val ects = ectsText.toFloatOrNull()

            // Performs validation on the form inputs before saving.
            when {
                ects == null -> ectsError = "Please enter a valid number" // Shows error if ECTS is not a valid number.
                ects < 0 -> ectsError = "ECTS cannot be negative" // Shows error if ECTS is a negative number.
                else -> {
                    ectsError = null // Clears any existing error.
                    // Creates a new CourseEntity with the data entered in the form.
                    val course = CourseEntity(
                        idCourse = id,
                        nameCourse = name,
                        ectsCourse = ects,
                        levelCourse = level
                    )
                    // Calls the ViewModel to insert the new course into the database.
                    viewModel.insertCourse(course)
                    // Invokes the onSaved callback to signal that the course has been saved and to navigate back.
                    onSaved()
                }
            }
        }) {
            Text("Save")
        }
    }
}
