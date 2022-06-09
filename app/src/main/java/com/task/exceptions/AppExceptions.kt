/**
 * Name: AppExceptions.kt
 * Created by: Nitin 9 Jun 2022
 * Copyright © 2022 GWL INC. All rights reserved.
 * Purpose: This file handles the application level exception.
 */

package com.task.exceptions

import com.task.data.error.FILE_NOT_FOUND
import com.task.data.error.UNKNOWN_ERROR
import java.io.FileNotFoundException

object AppExceptions {

    fun getErrorCode(throwable: Throwable): Int {
        return when (throwable) {
            is FileNotFoundException -> FILE_NOT_FOUND
            else -> UNKNOWN_ERROR
        }
    }
}