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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.firebaseforum.databinding.FragmentEditProfileBinding
import com.example.firebaseforum.firebase.FirebaseHandler
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.util.UUID

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var profileImage: AppCompatImageView
    private lateinit var description: EditText
    private lateinit var nickname: EditText
    private var profileImgChanged: Boolean = false
    private lateinit var profileUri: Uri
    private lateinit var  userUid: String
    private lateinit var subCost: EditText
    private lateinit var newMoney: EditText
    private lateinit var addMoney: Button
    private lateinit var sendMoney: Button
    private lateinit var moneyValue: TextView
    private val args: EditProfileFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this){
            findNavController().navigate(R.id.navigation_home)
        }
    }

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
        subCost = binding.subCost
        newMoney = binding.newMoney
        addMoney = binding.addMoney
        sendMoney = binding.sendMoney
        moneyValue = binding.moneyValue

        if(args.firstRegister){
            profileImage.setImageDrawable(resources.getDrawable(R.drawable.feet))
        }
        else{
            loadData()
        }

        addMoney.visibility = View.VISIBLE
        newMoney.visibility = View.GONE
        sendMoney.visibility = View.GONE
        binding.saveButton.setOnClickListener { saveButton() }
        binding.changeImg.setOnClickListener { pickImage() }
        addMoney.setOnClickListener { addMoneyButton() }
        sendMoney.setOnClickListener { sendMoneyButton() }
    }

    private fun sendMoneyButton(){
        FirebaseHandler.RealtimeDatabase.getUserMoneyRef(userUid).get().addOnSuccessListener {
            val oldMoney = it.value
            var money: Int = oldMoney.toString().toInt() + newMoney.text.toString().toInt()
            FirebaseHandler.RealtimeDatabase.setUserMoney(userUid,money)
            moneyValue.text = money.toString()
            addMoney.visibility = View.VISIBLE
            newMoney.visibility = View.GONE
            sendMoney.visibility = View.GONE
        }
    }
    private fun addMoneyButton(){
        addMoney.visibility = View.GONE
        newMoney.visibility = View.VISIBLE
        sendMoney.visibility = View.VISIBLE
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
            if(!subCost.text.isEmpty())
                FirebaseHandler.RealtimeDatabase.setSubscriptionCost(subCost.text.toString().toInt())
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
        FirebaseHandler.RealtimeDatabase.getImage(userUid).getBytes(4196*4196).addOnSuccessListener {
            val image = it.toBitmap()
            profileImage.setImageBitmap(image)
        }

        FirebaseHandler.RealtimeDatabase.getNicknameRef().get().addOnSuccessListener {
            nickname.setText(it.value.toString())
        }

        FirebaseHandler.RealtimeDatabase.getDescriptionRef().get().addOnSuccessListener {
            description.setText(it.value.toString())
        }

        FirebaseHandler.RealtimeDatabase.getUserMoneyRef(userUid).get().addOnSuccessListener {
            moneyValue.text = it.value.toString()
        }

        FirebaseHandler.RealtimeDatabase.getSubscriptionCostRef(userUid).get().addOnSuccessListener {
            subCost.setText(it.value.toString())
        }
    }

    private fun ByteArray.toBitmap(): Bitmap {
        return BitmapFactory.decodeByteArray(this, 0, this.size)
    }
}