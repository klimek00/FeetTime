package com.example.firebaseforum.ui.login

import android.os.Bundle
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.firebaseforum.R
import kotlin.Boolean
import kotlin.Int

public class LoginRegisterFragmentDirections private constructor() {
  private data class ActionNavigationLoginToEditProfileFragment(
    public val firstRegister: Boolean = false
  ) : NavDirections {
    public override val actionId: Int = R.id.action_navigation_login_to_editProfileFragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putBoolean("firstRegister", this.firstRegister)
        return result
      }
  }

  public companion object {
    public fun actionNavigationLoginToNavigationHome(): NavDirections =
        ActionOnlyNavDirections(R.id.action_navigation_login_to_navigation_home)

    public fun actionNavigationLoginToEditProfileFragment(firstRegister: Boolean = false):
        NavDirections = ActionNavigationLoginToEditProfileFragment(firstRegister)
  }
}
