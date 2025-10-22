package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.Gender
import com.tumme.scrudstudents.data.local.model.StudentEntity

@Composable
fun StudentFormScreen(
    //injects the StudentListViewModel using Hilt for data operations.
    viewModel: StudentListViewModel = hiltViewModel(),
    //callback function to be invoked when the form is saved.
    onSaved: ()->Unit = {}
) {
    //creates a mutable state for the student's ID, initialized with a random number. 'remember' ensures the state survives recomposition.
    var id by remember { mutableStateOf((0..10000).random()) }
    //same mutable state for the surname but with a default empty string
    var lastName by remember { mutableStateOf("") }
    //same as surname but for the first name
    var firstName by remember { mutableStateOf("") }
    //same as firstname but for the date of birth
    var dobText by remember { mutableStateOf("2000-01-01") } // The expected format is yyyy-MM-dd.
    //same as before but with default value for gender
    var gender by remember { mutableStateOf(Gender.NotConcerned) }

    //formats the date of birth to string
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        //lastName state is updated on every value change.
        TextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") })
        Spacer(Modifier.height(8.dp))
        //same as the surname
        TextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") })
        Spacer(Modifier.height(8.dp))
        //same as firstname
        TextField(value = dobText, onValueChange = { dobText = it }, label = { Text("Date of birth (yyyy-MM-dd)") })
        Spacer(Modifier.height(8.dp))

        //row for gender selection buttons.
        Row {
            //iterates through the available Gender options to create a button for each.
            listOf(Gender.Male, Gender.Female, Gender.NotConcerned).forEach { g->
                //updates the gender state to the selected gender.
                Button(onClick = { gender = g }, modifier = Modifier.padding(end = 8.dp)) {
                    Text(g.value)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        //main save button for the form.
        Button(onClick = {
            //parses the date of birth text into a Date object. If parsing fails, it uses the current date as a fallback.
            val dob = dateFormat.parse(dobText) ?: Date()
            //creates a new StudentEntity with the data entered in the form.
            val student = StudentEntity(
                idStudent = id,
                lastName = lastName,
                firstName = firstName,
                dateOfBirth = dob,
                gender = gender
            )
            //calls the ViewModel to insert the new student into the database.
            viewModel.insertStudent(student)
            //invokes the onSaved callback to signal that the student has been saved.
            onSaved()
        }) {
            Text("Save")
        }
    }
}
//in this case the flow of interaction in inverted: it starts from the view model where the user is interacting
//with the ui, then it calls the view model to update the model layer of the database.