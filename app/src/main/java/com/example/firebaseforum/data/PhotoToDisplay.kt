package com.example.firebaseforum.data

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoToDisplay(
    val username: String,
    val photo: Bitmap,
    val userUid: String
): Parcelable