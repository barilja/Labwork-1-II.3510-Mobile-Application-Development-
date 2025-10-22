package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
@Entity(tableName = "teachers")
data class TeacherEntity (
    @PrimaryKey val idTeacher:Int,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: Date,
    val email:String,
    val password:String
)

