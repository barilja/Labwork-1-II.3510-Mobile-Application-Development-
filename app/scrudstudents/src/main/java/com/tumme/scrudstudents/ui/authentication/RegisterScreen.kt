package com.tumme.scrudstudents.ui.authentication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.Gender
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.ui.student.StudentListViewModel
import com.tumme.scrudstudents.ui.teacher.TeacherViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    studentViewModel: StudentListViewModel = hiltViewModel(),
    teacherViewModel: TeacherViewModel = hiltViewModel(),
    onRegistered: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {}
) {
    var userType by remember { mutableStateOf("Student") }

    // Shared fields
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dateOfBirthText by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Student-specific field
    var gender by remember { mutableStateOf(Gender.Male) }

    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Register") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            // Toggle between Student / Teacher
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { userType = "Student" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (userType == "Student")
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) { Text("Student") }

                Button(
                    onClick = { userType = "Teacher" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (userType == "Teacher")
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) { Text("Teacher") }
            }

            Spacer(Modifier.height(16.dp))

            // Common input fields
            TextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            TextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            TextField(value = dateOfBirthText, onValueChange = { dateOfBirthText = it }, label = { Text("Date of Birth (yyyy-MM-dd)") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            TextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            // Student-specific input
            if (userType == "Student") {
                Spacer(Modifier.height(8.dp))
                Text("Gender:")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Gender.entries.forEach { g ->
                        Button(
                            onClick = { gender = g },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (gender == g)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text(g.value)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Show validation errors
            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    try {
                        val parsedDate = sdf.parse(dateOfBirthText)
                        if (parsedDate == null) {
                            errorMessage = "Invalid date format"
                            return@Button
                        }

                        if (email.isBlank() || password.isBlank()) {
                            errorMessage = "Email and password cannot be empty"
                            return@Button
                        }

                        if (userType == "Student") {
                            val student = StudentEntity(
                                idStudent = 0,
                                firstName = firstName,
                                lastName = lastName,
                                dateOfBirth = parsedDate,
                                gender = gender,
                                email = email,
                                password = password
                            )
                            studentViewModel.insertStudent(student)
                        } else {
                            val teacher = TeacherEntity(
                                idTeacher = 0,
                                firstName = firstName,
                                lastName = lastName,
                                dateOfBirth = parsedDate,
                                email = email,
                                password = password
                            )
                            teacherViewModel.insertTeacher(teacher)
                        }

                        onRegistered()

                    } catch (e: Exception) {
                        errorMessage = "Error: ${e.message}"
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Register")
            }
            Spacer(Modifier.height(12.dp))

            TextButton(
                onClick = { onNavigateToRegister() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Already have an account? Login!")
            }
        }
    }
}
