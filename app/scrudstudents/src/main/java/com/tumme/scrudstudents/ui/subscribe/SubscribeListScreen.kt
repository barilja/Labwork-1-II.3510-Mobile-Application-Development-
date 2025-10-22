package com.tumme.scrudstudents.ui.subscribe

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.ui.components.TableHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribeListScreen(
    viewModel: SubscribeViewModel = hiltViewModel(),
    onNavigateToForm: () -> Unit = {},
    onNavigateToDetail: (Int,Int,Float) -> Unit = {_,_,_->},//lambdas that pass more than 1 parameter
    onNavigateToEdit: (Int,Int,Float) -> Unit = {_,_,_->},
    //navigation lambdas for the footer
    onNavigateToCourses: () -> Unit = {},
    onNavigateToStudents: () -> Unit = {},
    onNavigateToSubscriptions: () -> Unit = {}
) {
    val subscribes by viewModel.subscribes.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Subscriptions") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToForm) {
                Text("+")
            }
        },
        //navigation footer
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Book, contentDescription = "Courses") },
                    label = { Text("Courses") },
                    selected = false,
                    onClick = onNavigateToCourses
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.People, contentDescription = "Students") },
                    label = { Text("Students") },
                    selected = false,
                    onClick = onNavigateToStudents
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Subscriptions") },
                    label = { Text("Subscriptions") },
                    selected = true,
                    onClick = onNavigateToSubscriptions
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            TableHeader(
                cells = listOf("Course", "Student", "Score", "Actions"),
                weights = listOf(0.25f, 0.25f, 0.25f, 0.25f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(subscribes) { subscribe ->
                    SubscribeRow(
                        subscribe = subscribe,
                        onEdit = { onNavigateToEdit(subscribe.courseId,subscribe.studentId,subscribe.score) },
                        onDelete = { viewModel.deleteSubscription(subscribe) },
                        onView = { onNavigateToDetail(subscribe.courseId,subscribe.studentId,subscribe.score) },
                        onShare = { }
                    )
                }
            }
        }
    }
}
