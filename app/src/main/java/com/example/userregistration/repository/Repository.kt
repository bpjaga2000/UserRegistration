package com.example.userregistration.repository

import com.example.userregistration.api.Envelope
import com.example.userregistration.bean.BaseBean
import com.example.userregistration.db.entities.RegionEntity
import com.example.userregistration.db.entities.UserEntity
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getData(): Flow<Envelope<BaseBean>>
    suspend fun addData(list: List<RegionEntity>)
    fun getCountries(): Flow<List<RegionEntity>?>
    fun checkUserName(userName: String): Flow<Int?>
    suspend fun addUser(userEntity: UserEntity)
    fun logInUser(userName: String, password: String): Flow<UserEntity?>
}