package com.example.firebaseforum.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
  val author:String? = null,
  val message:String? = null,
  val timestamp: Long? = null
): Parcelable

