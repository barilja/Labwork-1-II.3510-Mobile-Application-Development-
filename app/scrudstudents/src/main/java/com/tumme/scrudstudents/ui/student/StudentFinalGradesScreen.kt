package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.ui.course.CourseViewModel
import com.tumme.scrudstudents.ui.subscribe.SubscribeViewModel
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.data.local.model.CourseEntity
import android.graphics.pdf.PdfDocument
import android.os.Environment
import androidx.compose.ui.res.stringResource
import com.tumme.scrudstudents.R
import java.io.File
import java.io.FileOutputStream
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentFinalGradesScreen(
    studentId: Int,
    subscribeViewModel: SubscribeViewModel = hiltViewModel(),
    courseViewModel: CourseViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {

    //variable to export the screen as pdf
    var showExportMessage by remember { mutableStateOf(false) }
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
                title = { Text(stringResource(R.string.final_grades_student)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                exportToPdf(studentSubscribes, courses, studentId)
                showExportMessage = true
            }) {
                Text(stringResource(R.string.pdf_button))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (showExportMessage) {
                Text(stringResource(R.string.pdf_exported_to_downloads), color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                stringResource(
                    R.string.final_grade,
                    finalGrade?.let { String.format("%.2f", it) } ?: "No grades yet"),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Table header
            Row(modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)) {
                Text(stringResource(R.string.course_id_grade), modifier = Modifier.width(100.dp))
                Text(stringResource(R.string.course_name_grade), modifier = Modifier.width(200.dp))
                Text(stringResource(R.string.ects_grade), modifier = Modifier.width(80.dp))
                Text(stringResource(R.string.score_grade), modifier = Modifier.width(100.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // List of courses with scores
            LazyColumn {
                items(studentSubscribes) { sub ->
                    val course = courses.find { it.idCourse == sub.courseId }
                    if (course != null) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(scrollState)
                            .padding(vertical = 4.dp)) {
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

fun exportToPdf(subscribes: List<SubscribeEntity>, courses: List<CourseEntity>, studentId: Int) {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size in points
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    val paint = android.graphics.Paint()
    paint.textSize = 12f

    var y = 30f

    // Header
    canvas.drawText("Student $studentId - Final Grades", 30f, y, paint)
    y += 30f

    // Table header
    canvas.drawText("Course ID", 30f, y, paint)
    canvas.drawText("Course Name", 120f, y, paint)
    canvas.drawText("ECTS", 350f, y, paint)
    canvas.drawText("Score", 420f, y, paint)
    y += 20f

    // Table content
    subscribes.forEach { sub ->
        val course = courses.find { it.idCourse == sub.courseId }
        if (course != null) {
            canvas.drawText(course.idCourse.toString(), 30f, y, paint)
            canvas.drawText(course.nameCourse, 120f, y, paint)
            canvas.drawText(course.ectsCourse.toString(), 350f, y, paint)
            canvas.drawText(if (sub.score > 0f) sub.score.toString() else "Not evaluated", 420f, y, paint)
            y += 20f
        }
    }

    pdfDocument.finishPage(page)

    val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val file = File(downloads, "Student_${studentId}_FinalGrades.pdf")
    pdfDocument.writeTo(FileOutputStream(file))
    pdfDocument.close()
}
