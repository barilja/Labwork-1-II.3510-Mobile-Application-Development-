package com.tumme.scrudstudents.ui.teacher

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.ui.subscribe.SubscribeViewModel
import com.tumme.scrudstudents.ui.course.CourseViewModel
import com.tumme.scrudstudents.ui.teaches.TeachViewModel
import com.tumme.scrudstudents.ui.components.TableHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherMarksScreen(
    teacherId: Int, // ID of the currently logged-in teacher
    teachViewModel: TeachViewModel = hiltViewModel(), // ViewModel for teaches (teacher-course relationships)
    subscribeViewModel: SubscribeViewModel = hiltViewModel(), // ViewModel for student subscriptions
    courseViewModel: CourseViewModel = hiltViewModel(), // ViewModel for courses
    onNavigateBack: () -> Unit = {} // Callback to navigate back
) {
    // Collect state flows from ViewModels to observe changes in real-time
    val teaches by teachViewModel.teaches.collectAsState() // All teaches in DB
    val subscribes by subscribeViewModel.subscribes.collectAsState() // All subscriptions
    val courses by courseViewModel.courses.collectAsState() // All courses

    // Filter courses that belong to this teacher
    val teacherCourseIds = teaches.filter { it.teacherId == teacherId }.map { it.courseId }

    // Filter subscriptions only for this teacher's courses
    val teacherSubscribes = subscribes.filter { it.courseId in teacherCourseIds }

    val scrollState = rememberScrollState() // For horizontal scrolling

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Assign Marks") }, // Title of the page
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { // Back button
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Display table header with column names and proportional weights
            TableHeader(
                cells = listOf("Student ID", "Course ID", "Course Name", "Score", "Action"),
                weights = listOf(0.15f, 0.15f, 0.35f, 0.15f, 0.2f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Show a message if there are no students in this teacher's courses
            if (teacherSubscribes.isEmpty()) {
                Text("No students enrolled in your courses.")
            } else {
                // LazyColumn efficiently displays large lists
                LazyColumn {
                    items(
                        items = teacherSubscribes,
                        key = { "${it.studentId}_${it.courseId}" } // or just it.idSubscribe if you have one
                    ) { sub ->
                        val course = courses.find { it.idCourse == sub.courseId } // Find course name
                        TeacherMarkRow(
                            subscribe = sub, // Current subscription row
                            courseName = course?.nameCourse ?: "Loading...", // Course name or loading placeholder
                            onSaveScore = { score ->
                                // Save the new score in the database
                                val updated = sub.copy(score = score)
                                subscribeViewModel.insertSubscription(updated) // Insert/update subscription
                                // Optional: could show a confirmation here with Toast or Snackbar
                            },
                            scrollState = scrollState // Horizontal scroll for row
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TeacherMarkRow(
    subscribe: SubscribeEntity, // Student subscription for a course
    courseName: String, // Course name to display
    onSaveScore: (Float) -> Unit, // Callback to save the score
    scrollState: androidx.compose.foundation.ScrollState // Horizontal scroll state
) {
    // Holds the text in the score TextField
    var scoreText by remember { mutableStateOf(subscribe.score?.toString() ?: "") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .horizontalScroll(scrollState), // Allows horizontal scrolling if row is too wide
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Text(subscribe.studentId.toString(), modifier = Modifier.width(80.dp)) // Student ID
        Text(subscribe.courseId.toString(), modifier = Modifier.width(80.dp)) // Course ID
        Text(courseName, modifier = Modifier.width(200.dp)) // Course Name

        // TextField for entering score
        TextField(
            value = scoreText,
            onValueChange = { scoreText = it },
            label = { Text("Score") },
            modifier = Modifier.width(100.dp),
            singleLine = true
        )

        // Save button for the score
        IconButton(
            onClick = {
                val parsedScore = scoreText.toFloatOrNull() // Convert text to Float
                if (parsedScore != null) {
                    onSaveScore(parsedScore) // Call parent callback to save in DB
                }
            }
        ) {
            Icon(Icons.Default.Save, contentDescription = "Save Score")
        }
    }
    HorizontalDivider() // Row separator
}
