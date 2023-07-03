package com.example.firebaseforum.helpers

import android.content.Context
import android.os.IBinder
import android.view.inputmethod.InputMethodManager

object KeyboardHelper {
  fun hideSoftwareKeyboard(context: Context, windowToken: IBinder) {
// Get the InputMethodManager object
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
// Hide the software keyboard
    imm.hideSoftInputFromWindow(windowToken, 0)
  }
}