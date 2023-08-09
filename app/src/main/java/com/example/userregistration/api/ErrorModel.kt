package com.example.userregistration.api

data class ErrorModel(
    val errorCode: Int,
    val message: String = ""
)