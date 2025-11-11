package com.tumme.scrudstudents.ui.subscribe

import com.tumme.scrudstudents.ui.components.TableHeader
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribeListScreen(
    studentId: Int,
    viewModel: SubscribeViewModel = hiltViewModel(), // ViewModel injected via Hilt (MVVM dependency injection)
    onNavigateToForm: (Int) -> Unit = {}, // Navigation callback to open form screen (View/UI layer doesn't handle logic)
    onNavigateBack: () -> Unit = {} // Callback for navigation up
) {
    // Collects real-time state from ViewModel's Flow → triggers UI recomposition
    val subscribes by viewModel.subscribes.collectAsState()

    val scrollState = rememberScrollState() // UI scrolling, not MVVM logic

    // UI filters list locally — ViewModel provides all subscribes; View decides how to display for this student
    val studentSubscribes = subscribes.filter { it.studentId == studentId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Courses") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { // Delegates navigation to NavHost controller
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Home"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateToForm(studentId) }) {
                Text("+") // UI only triggers navigation to form, not DB logic
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            // Table header (pure UI component)
            TableHeader(
                cells = listOf("Course ID","course name","ECTS","Professor", "Score"),
                weights = listOf(0.5f,0.5f,0.5f,0.5f,0.5f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (studentSubscribes.isEmpty()) {
                // UI reaction to state result from ViewModel
                Text("You are not subscribed to any courses yet.")
            } else {
                // UI list renderer
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(studentSubscribes) { subscribe ->

                        // Child row that displays one subscription
                        // ViewModel delete function triggered via callback → UI does not modify DB directly
                        SubscribeRow(
                            subscribe = subscribe,
                            onDelete = { viewModel.deleteSubscription(subscribe) } // MVVM: UI → ViewModel → Repository → DB
                        )
                    }
                }
            }
        }
    }
}
