package com.example.userregistration.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.userregistration.db.entities.RegionEntity
import com.example.userregistration.db.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRegions(list: List<RegionEntity>)

    @Query("Select * from regions")
    fun fetchRegions(): Flow<List<RegionEntity>?>

    @Query("Select count(1) from users where userName = :userName")
    fun checkUserName(userName: String): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(userEntity: UserEntity)

    @Query("Select * from users where userName = :userName and password = :password")
    fun verifyUser(userName: String, password: String): Flow<UserEntity?>

}