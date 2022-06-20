package com.task.data.dto.recipes

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val email: String = "",
    val latlng: String = "",
    val name: String = ""
) : Parcelable
