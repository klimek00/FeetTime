package com.example.firebaseforum.ui.home

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.firebaseforum.R
import kotlin.Int
import kotlin.String

public class HomeFragmentDirections private constructor() {
  private data class ActionNavigationHomeToUserProfileFragment(
    public val otherUserID: String
  ) : NavDirections {
    public override val actionId: Int = R.id.action_navigation_home_to_userProfileFragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("otherUserID", this.otherUserID)
        return result
      }
  }

  public companion object {
    public fun actionNavigationHomeToUserProfileFragment(otherUserID: String): NavDirections =
        ActionNavigationHomeToUserProfileFragment(otherUserID)
  }
}
