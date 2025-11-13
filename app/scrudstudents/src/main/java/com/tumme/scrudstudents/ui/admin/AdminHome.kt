package com.tumme.scrudstudents.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.R
import com.tumme.scrudstudents.ui.student.StudentListViewModel
import com.tumme.scrudstudents.ui.teacher.TeacherViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHome(
    id:Int,
    teacherViewModel: TeacherViewModel = hiltViewModel(),
    studentViewModel: StudentListViewModel = hiltViewModel(),
    onLogout: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()

    // Collect reactive student list from StateFlow
    val students by studentViewModel.students.collectAsState()

    // Assuming TeacherViewModel has a similar `teachers` flow
    val teachers by teacherViewModel.teachers.collectAsState(initial = emptyList())

    val snackbarHostState = remember { SnackbarHostState() }

    //internationalization of errors
    val teacherDeletedMessage = stringResource(R.string.teacher_deleted_message)
    val studentDeletedMessage = stringResource(R.string.student_deleted_message)

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.admin_dashboard)) }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(stringResource(R.string.teachers_admin_home), style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))

            if (teachers.isEmpty()) {
                Text(stringResource(R.string.no_teachers_found))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(teachers) { teacher ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        stringResource(
                                            R.string.teacher_name_admin_home,
                                            teacher.firstName,
                                            teacher.lastName
                                        ))
                                    Text(teacher.email, style = MaterialTheme.typography.bodySmall)
                                }
                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            teacherViewModel.deleteTeacher(teacher)
                                            snackbarHostState.showSnackbar(teacherDeletedMessage)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer
                                    )
                                ) {
                                    Text(stringResource(R.string.delete_admin_function))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(stringResource(R.string.students_admin_home), style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))

            if (students.isEmpty()) {
                Text(stringResource(R.string.no_students_found))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(students) { student ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        stringResource(
                                            R.string.student_name_Admin_home,
                                            student.firstName,
                                            student.lastName
                                        ))
                                    Text(student.email, style = MaterialTheme.typography.bodySmall)
                                }
                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            studentViewModel.deleteStudent(student)
                                            snackbarHostState.showSnackbar(studentDeletedMessage)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer
                                    )
                                ) {
                                    Text(stringResource(R.string.delete_admin_function))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { onLogout() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(R.string.logout))
            }
        }
    }
}
