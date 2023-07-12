package com.example.firebaseforum.data

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


//password for images?
@Parcelize
data class Image(
  val title: String?           = null,
  val description: String?         = null,
  val ownerId: String?            = null,
  val ownerEmail: String?         = null,
  val filename: String?            = null,
  var private: Boolean?         = false,
): Parcelable