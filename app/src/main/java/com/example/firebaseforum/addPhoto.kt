package com.example.firebaseforum

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.fragment.findNavController
import com.example.firebaseforum.data.Image
import com.example.firebaseforum.databinding.FragmentAddPhotoBinding
import com.example.firebaseforum.firebase.FirebaseHandler
import com.google.android.material.snackbar.Snackbar
import java.util.*

class addPhoto : Fragment() {
  private var _binding: FragmentAddPhotoBinding? = null
  private lateinit var newImage: AppCompatImageView
  private var uri: Uri? = null
  private var switch: Boolean = false


  private val selectImageActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
    if (it.resultCode === Activity.RESULT_OK){
      uri = it?.data?.data
      binding.imagePreview.setImageURI(uri)
    }
  }
  private fun selectImage() {
    val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
    selectImageActivity.launch(pickImg)
  }


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    _binding = FragmentAddPhotoBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
//    binding.imageDescription.setText("zmieniony")
    if (binding.imagePreview.getDrawable() == null) {
      binding.imagePreview.setImageResource(R.drawable.feet)
    }

    binding.subSwitch.setOnCheckedChangeListener { _, isChecked ->
      switch = isChecked
    }

    binding.addImage.setOnClickListener {
      selectImage()
    }

    binding.saveButton.setOnClickListener {
      val title = binding.imageTitle.text.toString()
      val description = binding.imageDescription.text.toString()

      if (title.isEmpty() || description.isEmpty()) {
        Snackbar.make(
          binding.root, "Wszystkie pola musza byc wypelnione!",
          Snackbar.LENGTH_SHORT
        ).show()
      } else {
        //generate unique name for file
        val filename = UUID.randomUUID().toString()

        val newImage = Image(
          title,
          description,
          FirebaseHandler.Authentication.getUserUid(),
          FirebaseHandler.Authentication.getUserEmail(),
          filename,
          switch,
        )
        FirebaseHandler.RealtimeDatabase.addImage(newImage, uri)

        Snackbar.make(
          binding.root, "pomyslnie wrzucono!",
          Snackbar.LENGTH_SHORT
        ).show()
        findNavController().navigate(R.id.action_navigation_addPhotoFragment_to_navigation_home)
      }
    }
  }

  private val binding get() = _binding!!
  // This method is called when the view is destroyed.
  override fun onDestroyView() {
    _binding = null
    super.onDestroyView()
  }
}