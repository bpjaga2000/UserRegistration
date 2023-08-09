package com.example.userregistration.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.userregistration.db.dao.AppDao
import com.example.userregistration.db.entities.RegionEntity
import com.example.userregistration.db.entities.UserEntity

@Database(entities = [RegionEntity::class, UserEntity::class], version = 1, exportSchema = false)
abstract class UserRegistrationDb : RoomDatabase() {
    abstract fun appDao(): AppDao
}