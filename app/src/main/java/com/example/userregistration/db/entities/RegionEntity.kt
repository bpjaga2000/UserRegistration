package com.example.userregistration.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "regions")
data class RegionEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("code")
    var code: String,
    @ColumnInfo("name")
    var name: String,
    @ColumnInfo("region")
    var region: String
)
