package com.example.firebaseforum.helpers

import java.text.SimpleDateFormat
import java.util.*

fun Long.toDateString(): String {
  val sdf = SimpleDateFormat(
    "dd.MM.yyyy HH:mm",
    Locale.getDefault()
  )
  return sdf.format(Date(this))
}