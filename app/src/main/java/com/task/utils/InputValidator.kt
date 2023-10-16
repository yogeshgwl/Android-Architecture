package com.task.utils

import android.util.Patterns
import com.task.R

/**
 * This file to handle the screen input validations.
 */
object InputValidator {

    fun getPasswordErrorIdOrNull(input: String): Int? {
        return when {
            input.length < 5 -> R.string.password_short
            else -> null
        }
    }

    fun getEmailErrorIdOrNull(input: String): Int? {
        return when {
            !Patterns.EMAIL_ADDRESS.matcher(input)
                .matches() -> R.string.email_address_not_match_error
            //etc..
            else -> null
        }
    }

}
