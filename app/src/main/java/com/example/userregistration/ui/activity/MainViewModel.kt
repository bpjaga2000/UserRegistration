package com.example.userregistration.ui.activity

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userregistration.BuildConfig
import com.example.userregistration.R
import com.example.userregistration.api.Envelope
import com.example.userregistration.db.entities.RegionEntity
import com.example.userregistration.db.entities.UserEntity
import com.example.userregistration.repository.Repository
import com.example.userregistration.utils.AESEncryption
import com.example.userregistration.utils.isPasswordValidForLogIn
import com.example.userregistration.utils.isPasswordValidForRegistration
import com.example.userregistration.utils.isUserNameValid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class MainViewModel(
    private val repo: Repository
) : ViewModel() {

    val progressBoolean = ObservableBoolean(false)
    val isEnabled = ObservableBoolean(false)
    val toolBarText = ObservableField("app")

    val userName = MutableLiveData<String?>(null)
    val password = MutableLiveData<String?>(null)
    val region = MutableLiveData<Int?>(null)

    private val _checkUser = MutableStateFlow<Int?>(null)
    val checkUser get() = _checkUser.asStateFlow()

    private val _logIn = MutableStateFlow<UserEntity?>(null)
    val logIn get() = _logIn.asStateFlow()

    private val _countries = MutableStateFlow<List<RegionEntity>?>(null)
    val countries get() = _countries.asStateFlow()

    fun getRegion() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getData().map {
                if (it is Envelope.Success) {
                    val json = it.data.data
                    val keys = json.keySet()
                    val list = arrayListOf<RegionEntity>()

                    for (key in keys) {
                        val value = json[key].asJsonObject
                        list.add(
                            RegionEntity(
                                code = key,
                                name = value["country"].asString,
                                region = value["region"].asString
                            )
                        )
                    }
                    _countries.value = list
                }
            }.launchIn(viewModelScope)
        }
    }

    fun addToDb() {
        viewModelScope.launch(Dispatchers.IO) {
            _countries.value?.let {
                repo.addData(it)
            }
        }
    }

    fun getRegionsFromDb() {
        repo.getCountries().map {
            _countries.value = it ?: listOf()
        }.launchIn(viewModelScope)
    }

    fun checkUserName() {
        userName.value?.let { userName ->
            if (userName.isNotBlank())
                repo.checkUserName(userName).map {
                    _checkUser.value = it
                }.launchIn(viewModelScope)
        }
    }

    fun addUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val aes = AESEncryption(BuildConfig.KEY.toCharArray(), BuildConfig.SALT.toCharArray())
            repo.addUser(
                UserEntity(
                    userName = userName.value.orEmpty(),
                    password = aes.encrypt(password.value.orEmpty()).trim(),
                    regionCode = countries.value?.get(region.value ?: 0)?.code.orEmpty()
                )
            )
            logInUser()
        }
    }

    fun logInUser() {
        val aes = AESEncryption(BuildConfig.KEY.toCharArray(), BuildConfig.SALT.toCharArray())
        repo.logInUser(
            userName = userName.value.orEmpty(),
            password = aes.encrypt(password.value.orEmpty()).trim()
        ).map {
            _logIn.value = it ?: UserEntity()
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
                    password.value?.isPasswordValidForRegistration() == true &&
                    (region.value ?: 0) > 0
        )
    }

    fun logInEnable() {
        isEnabled.set(
            userName.value?.isUserNameValid() == true &&
                    password.value?.isPasswordValidForLogIn() == true
        )
    }

    fun passwordError(): Int =
        if (Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*[!@#$%&()]).{8,}$")
                .matcher(password.value).matches()
        )
            -1
        else if (!Pattern.compile("[0-9]+").matcher(password.value).find())
            R.string.password_needs_to_have_atleast_one_number
        else if (!Pattern.compile("[a-z]+").matcher(password.value).find())
            R.string.password_needs_to_have_atleast_one_lower_case
        else if (!Pattern.compile("[A-Z]+").matcher(password.value).find())
            R.string.password_needs_to_have_atleast_one_upper_case
        else if (!Pattern.compile("[!@#$%&()]+").matcher(password.value).find())
            R.string.password_needs_to_have_atleast_one_special_character
        else if (!Pattern.compile(".{8,}").matcher(password.value).find())
            R.string.password_needs_to_have_atleast_8_characters
        else -1

}