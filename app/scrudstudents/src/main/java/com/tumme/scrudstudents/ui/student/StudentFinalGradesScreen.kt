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
import android.content.Context
import androidx.compose.ui.platform.LocalContext
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentFinalGradesScreen(
    studentId: Int,
    subscribeViewModel: SubscribeViewModel = hiltViewModel(),
    courseViewModel: CourseViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {

    val context = LocalContext.current

    //variable to export the screen as pdf
    var showExportMessage by remember { mutableStateOf(false) }
    // Collect state from ViewModels
    val subscribes by subscribeViewModel.subscribes.collectAsState()
    val courses by courseViewModel.courses.collectAsState()

    // Filter subscriptions only for this student
    val studentSubscribes = subscribes.filter { it.studentId == studentId }

    // Scroll state for horizontal scrolling
    val scrollState = rememberScrollState()

    //internationalization of errors
    val noGradesYetMessage = stringResource(R.string.no_grades_yet)
    val notEvaluatedYetMessage = stringResource(R.string.not_evaluated_yet)

    // Compute weighted final grade: sum(score * ects)/sum(ects)
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
                exportToPdf(context, studentSubscribes, courses, studentId)
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
                    finalGrade?.let { String.format("%.2f", it) } ?: noGradesYetMessage),
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
                                text = if (sub.score > 0f) sub.score.toString() else notEvaluatedYetMessage,
                                modifier = Modifier.width(100.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

fun exportToPdf(
    context: Context, // Added Context parameter
    subscribes: List<SubscribeEntity>,
    courses: List<CourseEntity>,
    studentId: Int
) {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size in points
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    val paint = android.graphics.Paint()
    paint.textSize = 12f

    var y = 30f

    // Header - Used context to get localized string with formatting
    val headerText = context.getString(R.string.pdf_header_student_grades, studentId)
    canvas.drawText(headerText, 30f, y, paint)
    y += 30f

    // Table header - Used context to get localized strings
    canvas.drawText(context.getString(R.string.pdf_course_id_header), 30f, y, paint)
    canvas.drawText(context.getString(R.string.pdf_course_name_header), 120f, y, paint)
    canvas.drawText(context.getString(R.string.pdf_ects_header), 350f, y, paint)
    canvas.drawText(context.getString(R.string.pdf_score_header), 420f, y, paint)
    y += 20f

    // Localized string for not evaluated score
    val notEvaluatedText = context.getString(R.string.pdf_not_evaluated)

    // Table content
    subscribes.forEach { sub ->
        val course = courses.find { it.idCourse == sub.courseId }
        if (course != null) {
            canvas.drawText(course.idCourse.toString(), 30f, y, paint)
            canvas.drawText(course.nameCourse, 120f, y, paint)
            canvas.drawText(course.ectsCourse.toString(), 350f, y, paint)
            // Used localized string for "Not evaluated"
            val scoreText = if (sub.score > 0f) sub.score.toString() else notEvaluatedText
            canvas.drawText(scoreText, 420f, y, paint)
            y += 20f
        }
    }

    pdfDocument.finishPage(page)

    val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val file = File(downloads, "Student_${studentId}_FinalGrades.pdf")
    pdfDocument.writeTo(FileOutputStream(file))
    pdfDocument.close()
}
