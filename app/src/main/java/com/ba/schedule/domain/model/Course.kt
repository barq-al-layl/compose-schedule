package com.ba.schedule.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Course(
    val id: Int? = null,
    val name: String = "",
) : Parcelable
