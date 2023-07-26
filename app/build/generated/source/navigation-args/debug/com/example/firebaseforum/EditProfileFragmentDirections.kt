package com.example.firebaseforum

import android.os.Bundle
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import kotlin.Int
import kotlin.String

public class EditProfileFragmentDirections private constructor() {
  private data class ActionEditProfileFragmentToUserProfileFragment(
    public val otherUserID: String
  ) : NavDirections {
    public override val actionId: Int = R.id.action_editProfileFragment_to_userProfileFragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("otherUserID", this.otherUserID)
        return result
      }
  }

  public companion object {
    public fun actionEditProfileFragmentToUserProfileFragment(otherUserID: String): NavDirections =
        ActionEditProfileFragmentToUserProfileFragment(otherUserID)

    public fun actionEditProfileFragmentToNavigationHome(): NavDirections =
        ActionOnlyNavDirections(R.id.action_editProfileFragment_to_navigation_home)
  }
}
