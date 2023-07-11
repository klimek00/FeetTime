package com.example.firebaseforum

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.firebaseforum.databinding.FragmentEditProfileBinding
import com.example.firebaseforum.firebase.FirebaseHandler
import com.google.android.material.snackbar.Snackbar

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var profileImage: AppCompatImageView
    private lateinit var description: EditText
    private lateinit var nickname: EditText
    private val args: EditProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nickname = binding.nickEditText
        description = binding.descriptionInput
        profileImage = binding.profileImg
        if(args.firstRegister) profileImage.setImageDrawable(resources.getDrawable(R.drawable.feet))
        else loadData()

        binding.saveButton.setOnClickListener { saveButton() }
        binding.changeImg.setOnClickListener { pickImage() }
    }

    private fun saveButton() {
        if (nickname.text.toString().isEmpty()){
            Snackbar.make(
                binding.root, "Nickname nie może być pusty!",
                Snackbar.LENGTH_SHORT
            ).show()
        }else {
            findNavController().navigate(R.id.action_editProfileFragment_to_navigation_home)
            FirebaseHandler.RealtimeDatabase.addUserNickName(nickname.text.toString())
            FirebaseHandler.RealtimeDatabase.addUserDescription(description.text.toString())
        }
    }

    private fun pickImage(){
        val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        changeImage.launch(pickImg)
    }

    private val changeImage =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if(it.resultCode == Activity.RESULT_OK){
                val data = it.data
                val imgUri = data?.data
                profileImage.setImageURI(imgUri)
            }
        }

    private fun loadData(){
        //TO DO load picture from db
        nickname.setText(FirebaseHandler.RealtimeDatabase.getUserNickname())
        description.setText(FirebaseHandler.RealtimeDatabase.getUserDescription())
    }
}