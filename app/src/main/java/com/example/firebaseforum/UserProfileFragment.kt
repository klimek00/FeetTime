package com.example.firebaseforum

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.firebaseforum.data.Photos
import com.example.firebaseforum.data.UserPhotos
import com.example.firebaseforum.databinding.FragmentUserProfileBinding
import com.example.firebaseforum.databinding.FragmentUserProfileListBinding
import com.example.firebaseforum.firebase.FirebaseHandler
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

/**
 * A fragment representing a list of Items.
 */
class UserProfileFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileListBinding
    private val args: UserProfileFragmentArgs by navArgs()
    private var profile_data_loaded = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileListBinding.inflate(inflater,container,false)

        // Set the adapter
        with(binding.list){
            layoutManager = LinearLayoutManager(context)
            Photos.clearData()
            getPhotosData(args.otherUserID)
            while (getLoadingStatus()){
                //just wait for end of process
            }
            adapter = UserProfileRecyclerViewAdapter(Photos.ITEMS)
        }


        return binding.root
    }
    private fun getPhotosData(userID: String){
        var photoID: String? = null
        FirebaseHandler.RealtimeDatabase.getImagesReference().addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //TODO("Not yet implemented")
                for(images in snapshot.children){
                    for(data in images.children){
                        if(data.key == "ownerId"){
                            val ownerID = data.value.toString()
                            if (ownerID==userID){
                                photoID = images.key
                                Photos.addItem(
                                    UserPhotos(userID, photoID.toString())
                                )
                            }
                        }
                    }
                }
                profile_data_loaded = true
            }

            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
            }

        })
    }

    private fun getLoadingStatus(): Boolean = profile_data_loaded
    private fun clearData(){
        Photos.clearData()
        profile_data_loaded = false
    }

}