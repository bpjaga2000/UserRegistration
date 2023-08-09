package com.example.userregistration.ui.activity

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userregistration.BuildConfig
import com.example.userregistration.Utils.AESEncryption
import com.example.userregistration.Utils.isPasswordValid
import com.example.userregistration.Utils.isUserNameValid
import com.example.userregistration.api.Envelope
import com.example.userregistration.db.entities.RegionEntity
import com.example.userregistration.db.entities.UserEntity
import com.example.userregistration.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.json.JSONObject


class MainViewModel(
    private val repo: Repository
) : ViewModel() {

    val progressBoolean = ObservableBoolean(false)
    val isEnabled = ObservableBoolean(false)
    val toolBarText = ObservableField("app")

    val userName = MutableLiveData<String?>(null)
    val password = MutableLiveData<String?>(null)
    val region = MutableLiveData<Int?>(null)

    private val _checkUser = MutableStateFlow<Boolean?>(null)
    val checkUser get() = _checkUser.asStateFlow()

    private val _logIn = MutableStateFlow<UserEntity?>(null)
    val logIn get() = _logIn.asStateFlow()

    private val _countries = MutableStateFlow<List<RegionEntity>>(listOf())
    val countries get() = _countries.asStateFlow()

    fun getRegion() {
        viewModelScope.launch {
            repo.getData().map {
                if (it is Envelope.Success) {
                    val json = JSONObject(it.data.data)
                    val keys = json.keys()
                    val list = arrayListOf<RegionEntity>()

                    while (keys.hasNext()) {
                        val key = keys.next()
                        val value = JSONObject(json.getString(key))
                        list.add(
                            RegionEntity(
                                code = key,
                                name = value["country"].toString(),
                                region = value["region"].toString()
                            )
                        )
                    }
                    _countries.value = list
                    //repo.addData(list)
                }
            }.launchIn(viewModelScope)
        }
    }

    fun getRegionsFromDb() {
        repo.getCountries().map {
            _countries.value = it ?: listOf()
        }.launchIn(viewModelScope)
    }

    fun checkUserName() {
        userName.value?.let { userName ->
            repo.checkUserName(userName).map {
                _checkUser.value = (it ?: 0) > 0
            }.launchIn(viewModelScope)
        }
    }

    fun addUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val aes = AESEncryption(BuildConfig.KEY.toCharArray(), BuildConfig.SALT.toCharArray())
            repo.addUser(
                UserEntity(
                    userName = userName.value.orEmpty(),
                    password = aes.encrypt(password.value.orEmpty()),
                    regionCode = countries.value[region.value ?: 0].code
                )
            )
            logInUser()
        }
    }

    fun logInUser() {
        val aes = AESEncryption(BuildConfig.KEY.toCharArray(), BuildConfig.SALT.toCharArray())
        repo.logInUser(
            userName = userName.value.orEmpty(),
            password = aes.encrypt(password.value.orEmpty())
        ).map {
            _logIn.value = it
        }.launchIn(viewModelScope)
    }

    fun resetModels() {
        userName.value = null
        password.value = null
        region.value = null
        _checkUser.value = null
        _logIn.value = null
    }

    fun registerEnable() {
        isEnabled.set(
            userName.value?.isUserNameValid() == true &&
                    password.value?.isPasswordValid() == true &&
                    (region.value ?: 0) > 0
        )
    }

    fun logInEnable() {
        isEnabled.set(
            userName.value?.isUserNameValid() == true &&
                    password.value?.isPasswordValid() == true
        )
    }

}