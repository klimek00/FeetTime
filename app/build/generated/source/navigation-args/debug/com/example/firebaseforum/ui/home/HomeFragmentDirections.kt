package com.example.firebaseforum.ui.home

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.firebaseforum.R
import kotlin.Int
import kotlin.String

public class HomeFragmentDirections private constructor() {
  private data class ActionNavigationHomeToRoomFragment(
    public val roomName: String
  ) : NavDirections {
    public override val actionId: Int = R.id.action_navigation_home_to_roomFragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("roomName", this.roomName)
        return result
      }
  }

  public companion object {
    public fun actionNavigationHomeToRoomFragment(roomName: String): NavDirections =
        ActionNavigationHomeToRoomFragment(roomName)
  }
}
