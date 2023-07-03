package com.example.firebaseforum.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Room(
  val roomName: String?           = null,
  val ownerId: String?            = null,
  val ownerEmail: String?         = null,
  var lastMessage: String?        = null,
  var lastMessageAuthor: String?  = null,
  val lastMessageTimestamp: Long? = null,
  var isPrivate: Boolean?         = false,
  var roomPassword: String?       = null
): Parcelable