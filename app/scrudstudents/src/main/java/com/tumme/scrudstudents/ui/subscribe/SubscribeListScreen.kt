package com.tumme.scrudstudents.ui.subscribe

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
fun SubscribeListScreen(
    studentId: Int,
    viewModel: SubscribeViewModel = hiltViewModel(),
    onNavigateToForm: (Int) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val subscribes by viewModel.subscribes.collectAsState()
    val scrollState = rememberScrollState()

    // ✅ Filter subscriptions only for this student
    val studentSubscribes = subscribes.filter { it.studentId == studentId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Courses") },
                navigationIcon = {
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
            FloatingActionButton(onClick = { onNavigateToForm(studentId) }) {
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
                cells = listOf("Course ID", "Score"),
                weights = listOf(0.5f, 0.5f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (studentSubscribes.isEmpty()) {
                Text("You are not subscribed to any courses yet.")
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(studentSubscribes) { subscribe ->
                        SubscribeRow(
                            subscribe = subscribe,
                            onDelete = { viewModel.deleteSubscription(subscribe) }
                        )
                    }
                }
            }
        }
    }
}
