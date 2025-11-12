package com.tumme.scrudstudents.ui.authentication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.R
import com.tumme.scrudstudents.ui.student.StudentListViewModel
import com.tumme.scrudstudents.ui.teacher.TeacherViewModel
import com.tumme.scrudstudents.ui.admin.AdminViewModel
import com.tumme.scrudstudents.data.local.model.AdminEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    studentViewModel: StudentListViewModel = hiltViewModel(),
    teacherViewModel: TeacherViewModel = hiltViewModel(),
    adminViewModel: AdminViewModel=hiltViewModel(),
    onLoginSuccess: (role: String, userId: Int) -> Unit = { _, _ -> },
    onNavigateToRegister: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        // create the admin instance
        val admin = AdminEntity(
            idAdmin=1,
            email = "admin@gmail.com",
            password = "test"
        )
            adminViewModel.insertAdmin(admin)
    }

    var role by remember { mutableStateOf("Student") }
    var email by remember { mutableStateOf("test@gmail.com") }
    var password by remember { mutableStateOf("test") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // State to trigger snackbar and navigation
    var loginSuccess by remember { mutableStateOf<Pair<String, Int>?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.login_page)) }) },
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
            Text(stringResource(R.string.login_as))
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
                ) { Text(stringResource(R.string.student_button_login)) }

                Button(
                    onClick = { role = "Teacher" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (role == "Teacher")
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) { Text(stringResource(R.string.teacher_login_button)) }
                Button(
                    onClick = { role = "Admin" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (role == "Admin")
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) { Text(stringResource(R.string.admin_login_button)) }
            }

            Spacer(Modifier.height(16.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(Modifier.height(16.dp))

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
                                    loginSuccess = "Student" to student.idStudent
                                }
                            }
                            else if(role=="Admin"){
                                val admin=adminViewModel.getAdminByEmail(email)
                                if(admin==null){
                                    errorMessage="No admin found with this email."
                                } else if(admin.password!=password){
                                    errorMessage="Incorrect password."
                                } else {
                                    errorMessage=null
                                    loginSuccess="Admin" to admin.idAdmin
                                }
                            }
                            else {
                                val teacher = teacherViewModel.getTeacherByEmail(email)
                                if (teacher == null) {
                                    errorMessage = "No teacher found with this email."
                                } else if (teacher.password != password) {
                                    errorMessage = "Incorrect password."
                                } else {
                                    errorMessage = null
                                    loginSuccess = "Teacher" to teacher.idTeacher
                                }
                            }
                        } catch (_: Exception) {
                        }
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(R.string.login_button))
            }

            Spacer(Modifier.height(12.dp))

            TextButton(
                onClick = { onNavigateToRegister() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(R.string.don_t_have_an_account_register))
            }
        }

        // --- Snackbar and navigation after login ---
        loginSuccess?.let { (role, id) ->
            LaunchedEffect(loginSuccess) {
                snackbarHostState.showSnackbar("Login successful!")
                onLoginSuccess(role, id)
                loginSuccess = null
            }
        }
    }
}
