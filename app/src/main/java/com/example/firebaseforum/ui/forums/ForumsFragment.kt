package com.example.firebaseforum.ui.forums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.firebaseforum.databinding.FragmentForumsBinding


// The ForumsFragment is responsible for displaying the list of available chat rooms.
class ForumsFragment : Fragment() {
    // View binding for the fragment
    private var _binding: FragmentForumsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    // Inflate the layout for the fragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForumsBinding.inflate(inflater, container, false)
        return binding.root
    }


    // This function is called when the view is destroyed.
    override fun onDestroyView() {
        super.onDestroyView()
        // Set the binding object to null to avoid memory leaks.
        _binding = null
    }


}

