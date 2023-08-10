package com.example.userregistration.bean

import androidx.databinding.BaseObservable
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class BaseBean(
    @SerializedName("status")
    var status: String = "",
    @SerializedName("status-code")
    var code: Int = 0,
    @SerializedName("version")
    var version: String = "",
    @SerializedName("data")
    var data: JsonObject
) : BaseObservable()