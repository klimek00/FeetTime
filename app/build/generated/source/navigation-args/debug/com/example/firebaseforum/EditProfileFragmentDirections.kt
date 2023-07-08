package com.example.firebaseforum

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections

public class EditProfileFragmentDirections private constructor() {
  public companion object {
    public fun actionEditProfileFragmentToNavigationHome(): NavDirections =
        ActionOnlyNavDirections(R.id.action_editProfileFragment_to_navigation_home)
  }
}
