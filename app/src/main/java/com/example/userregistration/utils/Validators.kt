package com.example.userregistration.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.security.SecureRandom
import java.util.regex.Pattern

fun String.isUserNameValid(): Boolean = this.length in 4..16

fun String.isPasswordValidForLogIn(): Boolean = this.length in 8..32
fun String.isPasswordValidForRegistration(): Boolean = this.length in 8..32 &&
        Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*[!@#$%&()]).{8,}$")
            .matcher(this).matches()

fun generateRandomBytes(size: Int): ByteArray =
    ByteArray(size).apply {
        SecureRandom.getInstanceStrong().nextBytes(this)
    }

fun CharArray.toByteArray(): ByteArray {
    val b = ByteArray(this.size)
    for (i in this.indices)
        b[i] = this[i].code.toByte()
    return b
}

fun ByteArray.toCharArray(): CharArray {
    val c = CharArray(this.size)
    for (i in this.indices)
        c[i] = this[i].toInt().toChar()
    return c
}


fun <T> Fragment.collectLatestFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    this.lifecycleScope.launch {
        flow.collectLatest {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collectLatest(collect)
            }
        }
    }
}