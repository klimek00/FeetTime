package com.example.firebaseforum.ui.forums

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.String
import kotlin.jvm.JvmStatic

public data class RoomFragmentArgs(
  public val roomName: String
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putString("roomName", this.roomName)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("roomName", this.roomName)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): RoomFragmentArgs {
      bundle.setClassLoader(RoomFragmentArgs::class.java.classLoader)
      val __roomName : String?
      if (bundle.containsKey("roomName")) {
        __roomName = bundle.getString("roomName")
        if (__roomName == null) {
          throw IllegalArgumentException("Argument \"roomName\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"roomName\" is missing and does not have an android:defaultValue")
      }
      return RoomFragmentArgs(__roomName)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): RoomFragmentArgs {
      val __roomName : String?
      if (savedStateHandle.contains("roomName")) {
        __roomName = savedStateHandle["roomName"]
        if (__roomName == null) {
          throw IllegalArgumentException("Argument \"roomName\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"roomName\" is missing and does not have an android:defaultValue")
      }
      return RoomFragmentArgs(__roomName)
    }
  }
}
