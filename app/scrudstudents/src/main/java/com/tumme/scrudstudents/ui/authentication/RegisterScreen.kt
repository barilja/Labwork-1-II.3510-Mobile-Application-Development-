package com.tumme.scrudstudents.ui.authentication

import androidx.compose.foundation.horizontalScroll
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
import com.tumme.scrudstudents.data.local.model.LevelCourse
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.ui.student.StudentListViewModel
import com.tumme.scrudstudents.ui.teacher.TeacherViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.rememberScrollState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    studentViewModel: StudentListViewModel = hiltViewModel(),
    teacherViewModel: TeacherViewModel = hiltViewModel(),
    onRegistered: () -> Unit = {},        // called when registration is complete (navigate to login)
    onNavigateToRegister: () -> Unit = {} // optional callback to login page link
) {
    var userType by remember { mutableStateOf("Student") }

    // Shared fields
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dateOfBirthText by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var level by remember { mutableStateOf(LevelCourse.MS) }
    var scrollState=rememberScrollState()

    // Student-specific
    var gender by remember { mutableStateOf(Gender.Male) }

    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Snackbar host state for success message
    val snackbarHostState = remember { SnackbarHostState() }

    // State to trigger snackbar and navigation
    var registrationSuccess by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Register") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            // --- Switch between Student and Teacher ---
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

            // --- Input fields ---
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

            // --- Student-specific ---
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
                Row(modifier = Modifier.horizontalScroll(scrollState)) {
                    // Iterates through all available LevelCourse options to create a button for each.
                    LevelCourse.entries.forEach { courseLevel ->
                        // A button that, when clicked, updates the 'level' state to the selected course level.
                        Button(
                            onClick = { level = courseLevel },
                            modifier = Modifier.padding(end = 8.dp),
                            // Changes the button's color to indicate whether it is the currently selected level.
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (level == courseLevel)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Text(courseLevel.name)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // --- Error message ---
            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            // --- Register button ---
            Button(
                onClick = {
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
                            idStudent = (0..10000).random(),
                            firstName = firstName,
                            lastName = lastName,
                            dateOfBirth = parsedDate,
                            gender = gender,
                            email = email,
                            password = password,
                            levelCourse = level
                        )
                        studentViewModel.insertStudent(student)
                    } else {
                        val teacher = TeacherEntity(
                            idTeacher = (0..10000).random(),
                            firstName = firstName,
                            lastName = lastName,
                            dateOfBirth = parsedDate,
                            email = email,
                            password = password
                        )
                        teacherViewModel.insertTeacher(teacher)
                    }

                    errorMessage = null
                    registrationSuccess = true
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Register")
            }

            Spacer(Modifier.height(12.dp))

            // --- Already have account ---
            TextButton(
                onClick = { onNavigateToRegister() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Already have an account? Login!")
            }
        }

        // --- Snackbar and navigation effect ---
        if (registrationSuccess) {
            LaunchedEffect(registrationSuccess) {
                snackbarHostState.showSnackbar("Registration successful!")
                onRegistered()
                registrationSuccess = false
            }
        }
    }
}
