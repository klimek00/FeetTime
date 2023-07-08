package com.example.firebaseforum.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ownPhoto(
  val topic: String?           = null,
  val ownerId: String?            = null,
  val ownerEmail: String?         = null,
  val href: String?            = null,
  val description: String?         = null,
  var private: Boolean?         = false,
  var roomPassword: String?       = null
): Parcelable