package com.example.firebaseforum.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.firebaseforum.databinding.DialogAddRoomBinding


// Define a class called AddRoomDialog that extends DialogFragment
class AddRoomDialog : DialogFragment() {

    // Declare some private properties
    private lateinit var binding: DialogAddRoomBinding // Lazily initialize the binding variable later


    // Define a factory method for creating instances of the dialog
    fun newInstance(): AddRoomDialog {
        return AddRoomDialog()
    }



    // Override the onCreateView method to inflate the dialog layout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DialogAddRoomBinding.inflate(layoutInflater) // Inflate the dialog layout using the DialogAddRoomBinding class
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // Set the background of the dialog to be transparent
        return binding.root // Return the root view of the inflated layout
    }


    // Define an interface for the dialog listener
    interface DialogListener {
        fun onPositiveClick(roomName: String, password: String)
    }
}






