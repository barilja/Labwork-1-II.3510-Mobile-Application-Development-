package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.StudentEntity
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentHomeScreen(
    studentId: Int,
    viewModel: StudentListViewModel = hiltViewModel(),
    onNavigateToCourseList:(Int)->Unit={_->},
    onNavigateToStudentList:()->Unit={},
    onLogout:()->Unit={}
) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    var student by remember { mutableStateOf<StudentEntity?>(null) }

    LaunchedEffect(studentId) {
        student = viewModel.findStudent(studentId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Student Home Page") },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (student == null) {
                Text("Loading...")
            } else {
                Text("ID: ${student!!.idStudent}")
                Text("Name: ${student!!.firstName} ${student!!.lastName}")
                Text("Date of Birth: ${student!!.dateOfBirth.let { dateFormat.format(it) }}")
                Text("Email: ${student!!.email}")

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = { onNavigateToCourseList(student!!.idStudent) }) {
                    Text("Go to Course List")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(onClick = { onNavigateToStudentList()}) {
                    Text("Go to Student List")
                }

                Spacer(modifier=Modifier.height(12.dp))

                Button(onClick = { onLogout()}) {
                    Text("Logout")
                    Alignment.Center
                }
            }
        }
    }
}
