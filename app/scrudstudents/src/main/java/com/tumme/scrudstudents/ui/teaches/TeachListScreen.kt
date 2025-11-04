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
    viewModel: TeachViewModel = hiltViewModel(), // ViewModel provided by Hilt (dependency injection)
    onNavigateToForm: (Int) -> Unit = {},       // Callback to navigate to the form screen
    onNavigateBack: () -> Unit = {}             // Callback to navigate back to previous screen
) {
    // The ViewModel exposes a StateFlow; collectAsState converts it into Compose state for UI updates.
    val teaches by viewModel.teaches.collectAsState()

    // Scroll state retained across recompositions (useful if table grows).
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Courses") },
                navigationIcon = {
                    // The back button triggers navigation controlled from the ViewModel or NavGraph, not the UI.
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Home"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            // FAB triggers navigation to the course creation screen, passing the teacherId.
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
            // Table column headers (UI structure, not MVVM-critical).
            TableHeader(
                cells = listOf("ID", "Course Name", "ECTS", "Level"),
                weights = listOf(0.2f, 0.7f, 0.3f, 0.3f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // LazyColumn efficiently displays the list of courses exposed by the ViewModel.
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(teaches) { teach ->
                    // Each row displays a single Teach entity.
                    // Deleting triggers a ViewModel function, following the MVVM principle:
                    // UI only invokes actions, ViewModel handles logic and repository communication.
                    TeachRow(
                        teach = teach,
                        onDelete = { viewModel.deleteTeach(teach) }
                    )
                }
            }
        }
    }
}
