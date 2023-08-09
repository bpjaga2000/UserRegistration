package com.example.userregistration.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    var id: Int = 0,
    @ColumnInfo("userName")
    var userName: String,
    @ColumnInfo("password")
    var password: String,
    @ColumnInfo("regionCode")
    var regionCode: String
)