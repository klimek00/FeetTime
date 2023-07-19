package com.example.firebaseforum.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val email: String? = null,
val nickname: String? = null,
val description: String? =  null,
var uid: String? = null): Parcelable
