package com.example.firebaseforum.helpers
fun String.myCapitalize(): String {
  return this.replaceFirstChar {
    firstLetter: Char -> firstLetter.uppercase()
  }
}


