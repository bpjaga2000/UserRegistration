package com.example.userregistration.utils

import android.text.InputFilter
import java.util.regex.Pattern


object InputFilters {

    val usernameFilter = InputFilter { source, start, end, _, _, _ ->
        for (i in start until end) {
            if (!Pattern.compile("^[a-zA-Z0-9-_]+$").matcher(source).matches()) {
                return@InputFilter ""
            }
        }
        null
    }
    val passwordFilter = InputFilter { source, start, end, _, _, _ ->
        for (i in start until end) {
            if (!Pattern.compile("^[a-zA-Z0-9-_!@#\$%&()]+$").matcher(source).matches()) {
                return@InputFilter ""
            }
        }
        null
    }
}