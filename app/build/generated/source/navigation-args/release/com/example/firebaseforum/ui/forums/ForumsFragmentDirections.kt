package com.example.firebaseforum.ui.forums

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.firebaseforum.R
import kotlin.Int
import kotlin.String

public class ForumsFragmentDirections private constructor() {
  private data class ActionNavigationForumsToRoomFragment(
    public val roomName: String
  ) : NavDirections {
    public override val actionId: Int = R.id.action_navigation_forums_to_roomFragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("roomName", this.roomName)
        return result
      }
  }

  public companion object {
    public fun actionNavigationForumsToRoomFragment(roomName: String): NavDirections =
        ActionNavigationForumsToRoomFragment(roomName)
  }
}
