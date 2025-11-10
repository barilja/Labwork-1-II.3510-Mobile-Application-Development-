package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.AdminEntity

@Dao
interface AdminDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) //whenever a new teacher is inserted and it is a conflict then the strategy used is replace.
    suspend fun insert(admin: AdminEntity) // the suspend key word is useful to coroutines to not block the main UI thread and let the operation work asynchronously.

    @Delete
    suspend fun delete(admin: AdminEntity) //same reasoning as before used for delete operations on the entity.
    @Query("SELECT * FROM admins WHERE idAdmin = :id LIMIT 1") //query to search a specific admin by his id.
    suspend fun getAdminById(id: Int): AdminEntity? //same coroutine reasoning as before for the search operation + elvis operator to see if the admin actually exists.
    @Query("SELECT * FROM admins WHERE email = :email LIMIT 1")
    suspend fun getAdminByEmail(email: String): AdminEntity?
}
