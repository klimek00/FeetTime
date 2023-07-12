package com.example.firebaseforum

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.provider.MediaStore
import android.util.Log
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
import com.bumptech.glide.Glide
import com.example.firebaseforum.databinding.FragmentEditProfileBinding
import com.example.firebaseforum.firebase.FirebaseHandler
import com.google.android.material.snackbar.Snackbar
import java.util.UUID

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var profileImage: AppCompatImageView
    private lateinit var description: EditText
    private lateinit var nickname: EditText
    private var profileImgChanged: Boolean = false
    private lateinit var profileUri: Uri
    private lateinit var  userUid: String
    private val args: EditProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(layoutInflater,container,false)
        userUid = FirebaseHandler.Authentication.getUserUid().toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nickname = binding.nickEditText
        description = binding.descriptionInput
        profileImage = binding.profileImg
        if(args.firstRegister){
            profileImage.setImageDrawable(resources.getDrawable(R.drawable.feet))
        }
        else{
            loadData()
        }

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
            Log.d("UID: ", userUid)
            FirebaseHandler.RealtimeDatabase.addUserNickName(nickname.text.toString())
            FirebaseHandler.RealtimeDatabase.addUserDescription(description.text.toString())
            if(profileImgChanged)
                FirebaseHandler.RealtimeDatabase.uploadImage(userUid,profileUri)

            findNavController().navigate(R.id.action_editProfileFragment_to_navigation_home)
        }
    }

    private fun pickImage(){
        val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        changeImage.launch(pickImg)
        profileImgChanged = true
    }

    private val changeImage =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if(it.resultCode == Activity.RESULT_OK){
                val data = it.data
                //profileUri = data?.data!!
                val imgUri = data?.data
                if (imgUri != null) {
                    profileUri = imgUri
                }
                binding.profileImg.setImageURI(imgUri)
                //profileImage.setImageURI(imgUri)
            }
        }

    private fun loadData(){
        //TO DO load picture from db
        val uri = FirebaseHandler.RealtimeDatabase.getImageStorageRef(userUid)
        context?.let { Glide.with(it).load(uri).into(profileImage) }

        FirebaseHandler.RealtimeDatabase.getNicknameRef().get().addOnSuccessListener {
            nickname.setText(it.value.toString())
        }
        FirebaseHandler.RealtimeDatabase.getDescriptionRef().get().addOnSuccessListener {
            description.setText(it.value.toString())
        }
    }
}