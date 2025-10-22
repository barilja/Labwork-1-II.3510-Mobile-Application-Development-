package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.StudentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {

    @Query("SELECT * FROM students WHERE email = :email LIMIT 1")
    suspend fun getStudentByEmail(email: String): StudentEntity?

    @Query("SELECT * FROM students ORDER BY lastName, firstName") //query to obtain the list of all students ordered by their name.
    fun getAllStudents(): Flow<List<StudentEntity>> //the list of students is outputted in a flow that works asynchronously, so whenever the data is updated the flow is updated too.

    @Insert(onConflict = OnConflictStrategy.REPLACE) //whenever a new student is inserted and it is a conflict then the strategy used is replace.
    suspend fun insert(student: StudentEntity) // the suspend key word is useful to coroutines to not block the main UI thread and let the operation work asynchronously.

    @Delete
    suspend fun delete(student: StudentEntity) // same reasoning as before used for delete operations on the entity.

    @Query("SELECT * FROM students WHERE idStudent = :id LIMIT 1") //query to search a specific student by his id.
    suspend fun getStudentById(id: Int): StudentEntity? //same coroutine reasoning as before for the search operation + elvis operator to see if the student actually exists.
}
//part of the model layer as the student entity