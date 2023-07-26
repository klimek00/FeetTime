package com.example.firebaseforum.ui.forums

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.firebaseforum.R
import kotlin.Int
import kotlin.String

public class ForumsFragmentDirections private constructor() {
  private data class ActionNavigationForumsToUserProfileFragment(
    public val otherUserID: String
  ) : NavDirections {
    public override val actionId: Int = R.id.action_navigation_forums_to_userProfileFragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("otherUserID", this.otherUserID)
        return result
      }
  }

  public companion object {
    public fun actionNavigationForumsToUserProfileFragment(otherUserID: String): NavDirections =
        ActionNavigationForumsToUserProfileFragment(otherUserID)
  }
}
