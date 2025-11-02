package com.tumme.scrudstudents.ui.teacher

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.TeacherEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherHomeScreen(
    teacherId: Int,
    viewModel: TeacherViewModel = hiltViewModel(),
    onNavigateToCourseList:(Int)->Unit={_->},
    onNavigateToStudentList:()->Unit={}
) {
    var teacher by remember { mutableStateOf<TeacherEntity?>(null) }

    LaunchedEffect(teacherId) {
        teacher = viewModel.findTeacher(teacherId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Teacher Home Page") },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (teacher == null) {
                Text("Loading...")
            } else {
                Text("ID: ${teacher!!.idTeacher}")
                Text("Name: ${teacher!!.firstName} ${teacher!!.lastName}")
                Text("Date of Birth: ${teacher!!.dateOfBirth}")
                Text("Email: ${teacher!!.email}")

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = { onNavigateToCourseList(teacher!!.idTeacher) }) {
                    Text("Go to Course List")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(onClick = { onNavigateToStudentList()}) {
                    Text("Go to Student List")
                }
            }
        }
    }
}
