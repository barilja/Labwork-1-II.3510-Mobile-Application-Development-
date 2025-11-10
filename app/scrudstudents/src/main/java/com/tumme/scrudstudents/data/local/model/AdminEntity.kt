package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
@Entity(tableName = "admins")
data class AdminEntity (
    @PrimaryKey val idAdmin:Int,
    val email:String,
    val password:String
)

