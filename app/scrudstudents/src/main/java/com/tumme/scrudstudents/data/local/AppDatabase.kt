package com.tumme.scrudstudents.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tumme.scrudstudents.data.local.dao.CourseDao
import com.tumme.scrudstudents.data.local.dao.StudentDao
import com.tumme.scrudstudents.data.local.dao.SubscribeDao
import com.tumme.scrudstudents.data.local.dao.TeacherDao
import com.tumme.scrudstudents.data.local.dao.TeachDao
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.data.local.model.TeachEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity

// `entities`: Specifies all the table entities that belong in this database.
@Database(entities = [StudentEntity::class, CourseEntity::class, SubscribeEntity::class,
    TeacherEntity::class, TeachEntity::class], version = 1)
// The @TypeConverters annotation tells Room to use the defined Converters class for data type conversions (e.g., Date to Long).
@TypeConverters(Converters::class)
// This abstract class represents the app's database. It extends RoomDatabase.
abstract class AppDatabase : RoomDatabase() {
    //Declares an abstract function that returns an instance of StudentDao.
    //Room will generate the implementation for this method, providing access to the database operations for students.
    abstract fun studentDao(): StudentDao
    abstract fun courseDao(): CourseDao
    abstract fun subscribeDao(): SubscribeDao
    abstract fun teacherDao(): TeacherDao
    abstract fun teachDao(): TeachDao
}
