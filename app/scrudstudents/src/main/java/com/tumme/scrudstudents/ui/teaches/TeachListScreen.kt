package com.tumme.scrudstudents.ui.teaches

import com.tumme.scrudstudents.ui.components.TableHeader
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeachListScreen(
    teacherId: Int,
    viewModel: TeachViewModel = hiltViewModel(),
    onNavigateToForm: (Int) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val teaches by viewModel.teaches.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Courses") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { // 👈 back button
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Home"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateToForm(teacherId) }) {
                Text("+")
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            TableHeader(
                cells = listOf("ID", "Course Name", "ECTS", "Level"),
                weights = listOf(0.2f, 0.7f, 0.3f, 0.3f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(teaches) { teach ->
                    TeachRow(
                        teach = teach,
                        onDelete = { viewModel.deleteTeach(teach) }
                    )
                }
            }
        }
    }
}
