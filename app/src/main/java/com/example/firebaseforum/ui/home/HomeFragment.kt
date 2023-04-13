package com.example.firebaseforum.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.firebaseforum.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    // Get the binding object from the nullable _binding property. It will throw an exception
    // if accessed outside the lifecycle between onCreateView and onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the view using the generated binding class and set the _binding property.
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    // This method is called when the view is destroyed.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
