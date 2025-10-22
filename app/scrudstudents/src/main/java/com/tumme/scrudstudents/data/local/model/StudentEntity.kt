package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey val idStudent: Int, //unique id for the student
    val lastName: String, //several basic information regarding students
    val firstName: String,
    val dateOfBirth: Date,
    val gender: Gender
)
//part of the model layer that contains data useful to populate the UI view