package com.tumme.scrudstudents.ui.teaches

import com.tumme.scrudstudents.ui.components.TableHeader
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.R

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


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.your_courses_teacher)) },
                navigationIcon = {
                    // The back button triggers navigation controlled from the ViewModel or NavGraph, not the UI.
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_to_home_teacher)
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
                cells = listOf(stringResource(R.string.id_course_teacher),
                    stringResource(R.string.course_name_teacher),
                    stringResource(R.string.ects_teacher), stringResource(R.string.level_teacher)
                ),
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
