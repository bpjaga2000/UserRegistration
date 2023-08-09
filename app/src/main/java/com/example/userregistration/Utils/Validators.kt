package com.example.userregistration.Utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.security.SecureRandom

fun String.isUserNameValid(): Boolean = this.length in 4..16

fun String.isPasswordValid(): Boolean = this.length in 4..32

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