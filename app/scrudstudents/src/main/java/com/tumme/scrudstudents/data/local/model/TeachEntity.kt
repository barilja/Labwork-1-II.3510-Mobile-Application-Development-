package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index
import androidx.room.ColumnInfo

@Entity(
    tableName = "teach",
    primaryKeys = ["teacherId", "courseId"],
    foreignKeys = [
        ForeignKey(entity = TeacherEntity::class, parentColumns = ["idTeacher"], childColumns = ["teacherId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = CourseEntity::class, parentColumns = ["idCourse"], childColumns = ["courseId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("teacherId"), Index("courseId")]
)
data class TeachEntity(
    val teacherId: Int,
    val courseId: Int
)