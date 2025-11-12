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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.R
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherHomeScreen(
    teacherId: Int,
    viewModel: TeacherViewModel = hiltViewModel(),
    onNavigateToCourseList:(Int)->Unit={_->},
    onNavigateToStudentList:()->Unit={},
    onNavigateToMarkScreen:(Int)->Unit={_->},
    onLogout:()->Unit={}
) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    var teacher by remember { mutableStateOf<TeacherEntity?>(null) }

    LaunchedEffect(teacherId) {
        teacher = viewModel.findTeacher(teacherId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.teacher_home_page)) },
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
                Text(stringResource(R.string.loading_teacher))
            } else {
                Text(stringResource(R.string.id_teacher_home, teacher!!.idTeacher))
                Text(
                    stringResource(
                        R.string.name_teacher_home,
                        teacher!!.firstName,
                        teacher!!.lastName
                    ))
                Text(
                    stringResource(
                        R.string.date_of_birth_teacher_home,
                        teacher!!.dateOfBirth.let { dateFormat.format(it) }))
                Text(stringResource(R.string.email_teacher_home, teacher!!.email))

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = { onNavigateToCourseList(teacher!!.idTeacher) }) {
                    Text(stringResource(R.string.go_to_course_list_teacher))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(onClick = { onNavigateToStudentList()}) {
                    Text(stringResource(R.string.go_to_student_list_teacher))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(onClick = { onNavigateToMarkScreen(teacher!!.idTeacher)}) {
                    Text(stringResource(R.string.go_to_mark_screen_teacher))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(onClick = { onLogout()}) {
                    Text(stringResource(R.string.logout_teacher))
                }
            }
        }
    }
}
