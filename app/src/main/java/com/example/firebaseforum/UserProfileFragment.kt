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
import com.example.firebaseforum.databinding.FragmentUserProfileBinding
import com.example.firebaseforum.databinding.FragmentUserProfileListBinding

/**
 * A fragment representing a list of Items.
 */
class UserProfileFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileListBinding
    private val args: UserProfileFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileListBinding.inflate(inflater,container,false)

        // Set the adapter
        with(binding.list){
            layoutManager = LinearLayoutManager(context)
           /* Photos.clearData()
            Photos.getPhotosData(args.otherUserID)
            while (!Photos.getLoadingStatus()){
                //just wait for end of process
            }*/
            adapter = UserProfileRecyclerViewAdapter(Photos.ITEMS)
        }


        return binding.root
    }
}