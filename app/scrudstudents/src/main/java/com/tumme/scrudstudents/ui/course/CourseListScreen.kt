package com.tumme.scrudstudents.ui.course

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
import androidx.hilt.navigation.compose.hiltViewModel

// Opt-in to use experimental Material 3 APIs.
@OptIn(ExperimentalMaterial3Api::class)
// Defines a Composable function, which is the basic building block of a UI in Compose.
@Composable
fun CourseListScreen(
    // Injects the CourseViewModel using Hilt for data operations.
    viewModel: CourseViewModel = hiltViewModel(),
    // A lambda function to handle navigation to the course creation form.
    onNavigateToForm: () -> Unit = {},
    // A lambda function to handle navigation to the course detail screen.
    onNavigateToDetail: (Int) -> Unit = {},
    // A lambda function to handle navigation to the course edit screen.
    onNavigateToEdit: (Int) -> Unit = {},
    // Navigation callbacks for the footer menu.
    onNavigateToCourses: () -> Unit = {},
    onNavigateToStudents: () -> Unit = {},
    onNavigateToSubscriptions: () -> Unit = {}
) {
    // Collects the list of courses from the ViewModel as a State object. The UI will recompose when this state changes.
    val courses by viewModel.courses.collectAsState()
    // Creates a state for managing horizontal scrolling in the table rows.
    val scrollState = rememberScrollState()

    // Scaffold provides a standard layout structure with slots for top bar, footer, etc.
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Courses") })
        },
        floatingActionButton = {
            // A button for creating a new course, which triggers the onNavigateToForm callback.
            FloatingActionButton(onClick = onNavigateToForm) {
                Text("+")
            }
        },
        // Defines the bottom navigation bar for switching between main app sections.
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Book, contentDescription = "Courses") },
                    label = { Text("Courses") },
                    selected = true, // This item is marked as selected because we are on the Course screen.
                    onClick = onNavigateToCourses
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.People, contentDescription = "Students") },
                    label = { Text("Students") },
                    selected = false,
                    onClick = onNavigateToStudents
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Subscriptions") },
                    label = { Text("Subscriptions") },
                    selected = false,
                    onClick = onNavigateToSubscriptions
                )
            }
        }
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
                cells = listOf("ID", "Course Name", "ECTS", "Level", "Actions"),
                weights = listOf(0.2f, 0.6f, 0.3f, 0.3f, 0.3f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // vertically scrolling list that only composes and lays out the currently visible items.
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                // Iterates over the list of courses, creating a CourseRow for each one.
                items(courses) { course ->
                    CourseRow(
                        course = course,
                        onEdit = { onNavigateToEdit(course.idCourse) },
                        onDelete = { viewModel.deleteCourse(course) },
                        onView = { onNavigateToDetail(course.idCourse) },
                        onShare = { }
                    )
                }
            }
        }
    }
}
