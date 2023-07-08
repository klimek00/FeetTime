package com.example.firebaseforum

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.firebaseforum.databinding.ActivityMainBinding
import com.example.firebaseforum.databinding.FragmentAddPhotoBinding
import com.example.firebaseforum.databinding.FragmentHomeBinding

class addPhoto : Fragment() {
  private var binding: FragmentAddPhotoBinding? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = FragmentAddPhotoBinding.inflate(layoutInflater)
    binding!!.imageDescription.setText("zmieniony")

  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_add_photo, container, false)
  }

  // This method is called when the view is destroyed.
  override fun onDestroyView() {
    super.onDestroyView()
  }
}