package com.example.userregistration.repository

import com.example.userregistration.api.ApiService
import com.example.userregistration.api.Envelope
import com.example.userregistration.api.ErrorModel
import com.example.userregistration.bean.BaseBean
import com.example.userregistration.db.UserRegistrationDb
import com.example.userregistration.db.entities.RegionEntity
import com.example.userregistration.db.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AppRepository(
    private val api: ApiService,
    private val db: UserRegistrationDb
) : Repository {
    override suspend fun getData(): Flow<Envelope<BaseBean>> = flow {
        emit(Envelope.loading())
        try {
            val response = api.getData()
            if (response.isSuccessful)
                response.body()?.let {
                    emit(Envelope.success(it))
                }
            else
                response.errorBody()?.let {
                    emit(Envelope.error(ErrorModel(errorCode = response.code())))
                }
        } catch (e: Exception) {
            emit(Envelope.error(ErrorModel(503, e.message ?: "")))
        }
    }

    override suspend fun addData(list: List<RegionEntity>) {
        db.appDao().insertRegions(list)
    }

    override fun getCountries() = db.appDao().fetchRegions()

    override fun checkUserName(userName: String) = db.appDao().checkUserName(userName)

    override suspend fun addUser(userEntity: UserEntity) {
        return db.appDao().addUser(userEntity)
    }

    override fun logInUser(userName: String, password: String) =
        db.appDao().verifyUser(userName, password)
}