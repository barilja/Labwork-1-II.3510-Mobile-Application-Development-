package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import kotlinx.coroutines.flow.Flow

interface TeacherDao {

    @Query("SELECT * FROM teachers WHERE email = :email LIMIT 1")
    suspend fun getTeacherByEmail(email: String): TeacherEntity?

    @Query("SELECT * FROM teachers ORDER BY lastName, firstName") //query to obtain the list of all teachers ordered by their name.
    fun getAllTeachers(): Flow<List<TeacherEntity>> //the list of teachers is outputted in a flow that works asynchronously, so whenever the data is updated the flow is updated too.

    @Insert(onConflict = OnConflictStrategy.REPLACE) //whenever a new teacher is inserted and it is a conflict then the strategy used is replace.
    suspend fun insert(teacher: TeacherEntity) // the suspend key word is useful to coroutines to not block the main UI thread and let the operation work asynchronously.

    @Delete
    suspend fun delete(teacher: TeacherEntity) //same reasoning as before used for delete operations on the entity.

    @Query("SELECT * FROM teachers WHERE idTeacher = :id LIMIT 1") //query to search a specific teacher by his id.
    suspend fun getTeacherById(id: Int): TeacherEntity? //same coroutine reasoning as before for the search operation + elvis operator to see if the teacher actually exists.
}
