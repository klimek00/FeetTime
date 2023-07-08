package com.example.firebaseforum.ui.login

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.firebaseforum.R

public class LoginRegisterFragmentDirections private constructor() {
  public companion object {
    public fun actionNavigationLoginToNavigationHome(): NavDirections =
        ActionOnlyNavDirections(R.id.action_navigation_login_to_navigation_home)

    public fun actionNavigationLoginToEditProfileFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_navigation_login_to_editProfileFragment)
  }
}
