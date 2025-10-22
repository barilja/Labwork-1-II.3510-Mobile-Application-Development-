package com.tumme.scrudstudents.data.repository

import com.tumme.scrudstudents.data.local.dao.CourseDao
import com.tumme.scrudstudents.data.local.dao.StudentDao
import com.tumme.scrudstudents.data.local.dao.SubscribeDao
import com.tumme.scrudstudents.data.local.dao.TeacherDao
import com.tumme.scrudstudents.data.local.dao.TeachDao
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.local.model.TeachEntity
import kotlinx.coroutines.flow.Flow

class SCRUDRepository(
    private val studentDao: StudentDao, // Data Access Object for student-related database operations.
    private val courseDao: CourseDao,
    private val subscribeDao: SubscribeDao,
    private val teacherDao: TeacherDao,
    private val teachDao: TeachDao
) {
    //Students
    /*
      Retrieves all students from the database as a Flow.
      A Flow is an asynchronous data stream that emits values over time.
      Room automatically updates this Flow whenever the student data changes in the database.
      This makes it ideal for observing data in the UI layer.
      It returns a Flow that emits a list of all StudentEntity objects.
     */
    fun getAllStudents(): Flow<List<StudentEntity>> = studentDao.getAllStudents()

    /*
      Inserts a new student into the database.
      This is a suspend function, meaning it must be called from a coroutine or another suspend function.
      This ensures that the database operation does not block the main thread.
      @param student The StudentEntity object to be inserted.
     */
    suspend fun insertStudent(student: StudentEntity) = studentDao.insert(student)

    /*
      Deletes a student from the database.
      Like insertStudent, this is a suspend function to prevent blocking the main thread.
      @param student The StudentEntity object to be deleted.
     */
    suspend fun deleteStudent(student: StudentEntity) = studentDao.delete(student)

    /*
      Retrieves a single student from the database by their unique ID.
      This is a suspend function that performs the database query asynchronously.
      @param id The unique identifier of the student to retrieve.
      @return The corresponding StudentEntity, or null if no student is found with that ID.
     */
    suspend fun getStudentById(id: Int) = studentDao.getStudentById(id)

    // Courses
    fun getAllCourses(): Flow<List<CourseEntity>> = courseDao.getAllCourses()
    suspend fun insertCourse(course: CourseEntity) = courseDao.insert(course)
    suspend fun deleteCourse(course: CourseEntity) = courseDao.delete(course)
    suspend fun getCourseById(id: Int) = courseDao.getCourseById(id)

    // Subscribes
    fun getAllSubscribes(): Flow<List<SubscribeEntity>> = subscribeDao.getAllSubscribes()
    fun getSubscribesByStudent(sId: Int): Flow<List<SubscribeEntity>> = subscribeDao.getSubscribesByStudent(sId)
    fun getSubscribesByCourse(cId: Int): Flow<List<SubscribeEntity>> = subscribeDao.getSubscribesByCourse(cId)
    suspend fun insertSubscribe(subscribe: SubscribeEntity) = subscribeDao.insert(subscribe)
    suspend fun deleteSubscribe(subscribe: SubscribeEntity) = subscribeDao.delete(subscribe)
    fun getAllTeachers(): Flow<List<TeacherEntity>> = teacherDao.getAllTeachers()
    suspend fun insertTeacher(teacher: TeacherEntity) = teacherDao.insert(teacher)
    suspend fun deleteTeacher(teacher: TeacherEntity) = teacherDao.delete(teacher)
    suspend fun getTeacherById(id: Int) = teacherDao.getTeacherById(id)
    fun getAllTeaches(): Flow<List<TeachEntity>> = teachDao.getAllTeaches()
    suspend fun insertTeach(teach: TeachEntity) = teachDao.insert(teach)
    suspend fun deleteTeach(teach: TeachEntity) = teachDao.delete(teach)
    fun getTeachesByTeacher(tId: Int): Flow<List<TeachEntity>> = teachDao.getTeachesByTeacher(tId)
    fun getTeachesByCourse(cId: Int): Flow<List<TeachEntity>> = teachDao.getTeachesByCourse(cId)



}
//part of the model layer that mainly interacts with the viewmodel layer to update the view layer
