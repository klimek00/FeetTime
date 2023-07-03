package com.example.firebaseforum.helpers

import android.text.Editable
import android.text.TextWatcher
import java.util.*

class MyTextWatcher(private val taskListener: TimerTaskListener, private val delay:Long = 500) :
  TextWatcher {
  // Create a new timer instance to schedule the timer task
  private var timer = Timer()
  // Cancels and purges any existing timer when text is changed
  override fun onTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
    timer.cancel()
    timer.purge()
  }
  // After text is changed, a new timer is created and scheduled to run after the specified delay
  override fun afterTextChanged(editable: Editable) {
    timer = Timer()
    timer.schedule(object: TimerTask(){
      override fun run() {
        taskListener.timerRun()
      }
    }, delay)
  }
  override fun beforeTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {}
  // Cancels and purges the timer if necessary
  fun cancelTimer(){
    timer.cancel()
    timer.purge()
  }
}
// A listener interface for the timer task
interface TimerTaskListener{
  fun timerRun()
}