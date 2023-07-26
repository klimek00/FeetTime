package com.example.firebaseforum

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.firebaseforum.data.PhotoToDisplay
import com.example.firebaseforum.data.Photos
import com.example.firebaseforum.data.UserPhotos
import com.example.firebaseforum.databinding.FragmentUserProfileListBinding
import com.example.firebaseforum.firebase.FirebaseHandler
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

/**
 * A fragment representing a list of Items.
 */
class UserProfileFragment : Fragment(), ToDoListener {
    private lateinit var binding: FragmentUserProfileListBinding
    private val args: UserProfileFragmentArgs by navArgs()
    private val photos: ArrayList<Bitmap> = ArrayList()
    private val titles: ArrayList<String> = ArrayList()
    private val descriptions: ArrayList<String> = ArrayList()
    private lateinit var username: String
    private lateinit var profileDesc: String

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
        binding = FragmentUserProfileListBinding.inflate(inflater,container,false)
        username = "loading"
        profileDesc = "loading"

        // Set the adapter
        with(binding.list){
            layoutManager = LinearLayoutManager(context)
            Photos.clearData()
            clearData()
            getPhotosData(args.otherUserID)
            adapter = UserProfileRecyclerViewAdapter(Photos.ITEMS, photos, titles, descriptions, username, profileDesc, this@UserProfileFragment)
        }
        return binding.root
    }

    private fun getPhotosData(userID: String){
        var photoID: String? = null
        var ownerID: String? = null
        var title: String? = null
        var description: String? = null
        Photos.addItem(
            UserPhotos(userID, userID)
        )
        FirebaseHandler.RealtimeDatabase.getImagesReference().addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //TODO("Not yet implemented")
                for(images in snapshot.children){
                    for(data in images.children){
                        if (data.key == "description")
                            description = data.value.toString()
                        if(data.key == "ownerId"){
                            ownerID = data.value.toString()
                            if (ownerID==userID){
                                photoID = images.key
                                Photos.addItem(
                                    UserPhotos(userID, photoID.toString())
                                )
                            }
                        }
                        if (data.key == "title")
                            title = data.value.toString()
                    }
                    if (ownerID == userID){
                        titles.add(title.toString())
                        descriptions.add(description.toString())
                    }
                }
               loadPhoto()
            }

            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.otherUserID == FirebaseHandler.Authentication.getUserUid()) {
            binding.feetBtn.setOnClickListener() {
                findNavController().navigate(R.id.action_userProfileFragment_to_addPhotoFragment)
            }
            binding.feetBtn.visibility = View.VISIBLE
        } else {
            binding.feetBtn.visibility = View.INVISIBLE
        }
    }

    private fun loadPhoto(){
        if(Photos.ableToDownload()){
            FirebaseHandler.RealtimeDatabase.getImage(Photos.getNextElement().toString()).getBytes(4196*4196).addOnSuccessListener {
                var image = it.toBitmap()
                /*val matrix = Matrix()
                matrix.postRotate(90.0f)
                image = Bitmap.createBitmap(image,0,0,image.width,image.height,matrix,true)*/
                photos.add(image)
                loadPhoto()
            }
        }else{
            loadUsername()
        }
    }

    private fun loadUsername(){
        FirebaseHandler.RealtimeDatabase.getNicknameRef().get().addOnSuccessListener {
            username = it.value.toString()
            titles.add(0,username)
            loadProfileDesc()
        }
    }

    private fun loadProfileDesc(){
        FirebaseHandler.RealtimeDatabase.getDescriptionRef().get().addOnSuccessListener {
            profileDesc = it.value.toString()
            descriptions.add(0,profileDesc)
            loadAdapter()
        }
    }
    private fun loadAdapter(){
        with(binding.list){
            layoutManager = LinearLayoutManager(context)
            adapter = UserProfileRecyclerViewAdapter(Photos.ITEMS,photos,titles,descriptions, username, profileDesc, this@UserProfileFragment)
        }
    }

    private fun clearData(){
        Photos.clearData()
        photos.clear()
        titles.clear()
        descriptions.clear()
    }

    private fun ByteArray.toBitmap(): Bitmap {
        return BitmapFactory.decodeByteArray(this, 0, this.size)
    }

    override fun onItemClick(position: Int) {
        val action = UserProfileFragmentDirections.actionUserProfileFragmentToDisplayPhotoFragment(
            PhotoToDisplay(username,photos[position],args.otherUserID)
        )
        findNavController().navigate(action)
    }

}