package com.example.firebaseforum

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.firebaseforum.databinding.FragmentDisplayPhotoBinding


class DisplayPhotoFragment : Fragment() {
    private lateinit var binding: FragmentDisplayPhotoBinding
    private val args: DisplayPhotoFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this){
            val action = DisplayPhotoFragmentDirections.actionDisplayPhotoFragmentToUserProfileFragment(args.photo.userUid)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDisplayPhotoBinding.inflate(layoutInflater, container, false)

        binding.usernameMark.text = args.photo.username
        binding.imageView.setImageBitmap(args.photo.photo)

        return binding.root
    }

}