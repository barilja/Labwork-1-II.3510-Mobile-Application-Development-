package com.tumme.scrudstudents.ui.teaches

import androidx.compose.foundation.horizontalScroll
import com.tumme.scrudstudents.ui.components.TableHeader
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tumme.scrudstudents.ui.course.CourseRow
import androidx.hilt.navigation.compose.hiltViewModel

// Opt-in to use experimental Material 3 APIs.
@OptIn(ExperimentalMaterial3Api::class)
// Defines a Composable function, which is the basic building block of a UI in Compose.
@Composable
fun TeachListScreen(
    // Injects the CourseViewModel using Hilt for data operations.
    viewModel: TeachViewModel = hiltViewModel(),
    // A lambda function to handle navigation to the course creation form.
    onNavigateToForm: () -> Unit = {}
) {
    // Collects the list of courses from the ViewModel as a State object. The UI will recompose when this state changes.
    val teaches by viewModel.teaches.collectAsState()
    // Creates a state for managing horizontal scrolling in the table rows.
    val scrollState = rememberScrollState()

    // Scaffold provides a standard layout structure with slots for top bar, footer, etc.
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Your Courses") })
        },
        floatingActionButton = {
            // A button for creating a new course, which triggers the onNavigateToForm callback.
            FloatingActionButton(onClick = onNavigateToForm) {
                Text("+")
            }
        },
    ) { padding ->
        // The main content area of the screen.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Displays the header row for the course list table.
            TableHeader(
                cells = listOf("ID", "Course Name", "ECTS", "Level"),
                weights = listOf(0.2f, 0.7f, 0.3f, 0.3f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // vertically scrolling list that only composes and lays out the currently visible items.
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                // Iterates over the list of courses, creating a CourseRow for each one.
                items(teaches) { teach ->
                    TeachRow(
                        teach = teach,
                        onDelete = {(viewModel.deleteTeach(teach))}
                    )
                }
            }
        }
    }
}
