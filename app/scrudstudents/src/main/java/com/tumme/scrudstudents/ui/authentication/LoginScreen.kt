package com.tumme.scrudstudents.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.ui.student.StudentListViewModel
import com.tumme.scrudstudents.ui.teacher.TeacherViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    studentViewModel: StudentListViewModel = hiltViewModel(),
    teacherViewModel: TeacherViewModel = hiltViewModel(),
    onLoginSuccess: (role: String, userId: Int) -> Unit = { _, _ -> }
) {
    var role by remember { mutableStateOf("Student") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Login") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text("Login as:")
            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { role = "Student" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (role == "Student")
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text("Student")
                }
                Button(
                    onClick = { role = "Teacher" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (role == "Teacher")
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text("Teacher")
                }
            }

            Spacer(Modifier.height(16.dp))

            // Email input
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            // Password input
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(Modifier.height(16.dp))

            // Error message display
            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Please fill in all fields."
                        return@Button
                    }

                    coroutineScope.launch {
                        try {
                            if (role == "Student") {
                                val student = studentViewModel.getStudentByEmail(email)
                                if (student == null) {
                                    errorMessage = "No student found with this email."
                                } else if (student.password != password) {
                                    errorMessage = "Incorrect password."
                                } else {
                                    errorMessage = null
                                    onLoginSuccess("Student", student.idStudent)
                                }
                            } else {
                                val teacher = teacherViewModel.getTeacherByEmail(email)
                                if (teacher == null) {
                                    errorMessage = "No teacher found with this email."
                                } else if (teacher.password != password) {
                                    errorMessage = "Incorrect password."
                                } else {
                                    errorMessage = null
                                    onLoginSuccess("Teacher", teacher.idTeacher)
                                }
                            }
                        } catch (e: Exception) {
                            errorMessage = "Error: ${e.message}"
                        }
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Login")
            }
        }
    }
}
