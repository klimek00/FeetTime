package com.example.firebaseforum

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections

public class addPhotoDirections private constructor() {
  public companion object {
    public fun actionNavigationAddPhotoFragmentToNavigationHome(): NavDirections =
        ActionOnlyNavDirections(R.id.action_navigation_addPhotoFragment_to_navigation_home)
  }
}
