package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey val idCourse: Int,
    var nameCourse: String,
    val ectsCourse: Float,
    val levelCourse: LevelCourse
)