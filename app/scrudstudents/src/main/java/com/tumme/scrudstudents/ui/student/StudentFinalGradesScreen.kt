package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.ui.course.CourseViewModel
import com.tumme.scrudstudents.ui.subscribe.SubscribeViewModel
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import androidx.compose.material.icons.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentFinalGradesScreen(
    studentId: Int,
    subscribeViewModel: SubscribeViewModel = hiltViewModel(),
    courseViewModel: CourseViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    // Collect state from ViewModels
    val subscribes by subscribeViewModel.subscribes.collectAsState()
    val courses by courseViewModel.courses.collectAsState()

    // Filter subscriptions only for this student
    val studentSubscribes = subscribes.filter { it.studentId == studentId }

    // Scroll state for horizontal scrolling
    val scrollState = rememberScrollState()

    // Compute weighted final grade if you want: sum(score * ects)/sum(ects)
    val finalGrade = remember(studentSubscribes, courses) {
        var totalScore = 0f
        var totalEcts = 0f
        studentSubscribes.forEach { sub ->
            val course = courses.find { it.idCourse == sub.courseId }
            if (course != null && sub.score > 0f) { // consider only evaluated courses
                totalScore += sub.score * course.ectsCourse
                totalEcts += course.ectsCourse
            }
        }
        if (totalEcts > 0) totalScore / totalEcts else null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Final Grades") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(androidx.compose.material.icons.Icons.Default.ArrowBack, contentDescription = "Back")
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

            Text("Final Grade: ${finalGrade?.let { String.format("%.2f", it) } ?: "No grades yet"}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Table header
            Row(modifier = Modifier.fillMaxWidth().horizontalScroll(scrollState)) {
                Text("Course ID", modifier = Modifier.width(100.dp))
                Text("Course Name", modifier = Modifier.width(200.dp))
                Text("ECTS", modifier = Modifier.width(80.dp))
                Text("Score", modifier = Modifier.width(100.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // List of courses with scores
            LazyColumn {
                items(studentSubscribes) { sub ->
                    val course = courses.find { it.idCourse == sub.courseId }
                    if (course != null) {
                        Row(modifier = Modifier.fillMaxWidth().horizontalScroll(scrollState).padding(vertical = 4.dp)) {
                            Text(course.idCourse.toString(), modifier = Modifier.width(100.dp))
                            Text(course.nameCourse, modifier = Modifier.width(200.dp))
                            Text(course.ectsCourse.toString(), modifier = Modifier.width(80.dp))
                            Text(
                                text = if (sub.score > 0f) sub.score.toString() else "Not evaluated yet",
                                modifier = Modifier.width(100.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
