package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.TeachEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeachDao {
    @Query("SELECT * FROM teach")
    fun getAllTeaches(): Flow<List<TeachEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(teach: TeachEntity)

    @Delete
    suspend fun delete(teach: TeachEntity)

    @Query("SELECT * FROM teach WHERE teacherId = :tId")
    fun getTeachesByTeacher(tId: Int): Flow<List<TeachEntity>>

    @Query("SELECT * FROM teach WHERE courseId = :cId")
    fun getTeachesByCourse(cId: Int): Flow<List<TeachEntity>>
    @Query("SELECT * FROM teach WHERE courseId = :cId limit 1")
    suspend fun getTeachByCourse(cId: Int): TeachEntity?
}