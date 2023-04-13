package com.example.firebaseforum.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.firebaseforum.databinding.DialogRoomPasswordBinding

class RoomPasswordDialog : DialogFragment() {

    // Declare variables for view binding, dialog listener, and room position
    private lateinit var binding: DialogRoomPasswordBinding


    // Create a new instance of the RoomPasswordDialog fragment
    fun newInstance(): RoomPasswordDialog {
        return RoomPasswordDialog()
    }


    // Inflate the view for the dialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DialogRoomPasswordBinding.inflate(layoutInflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }


    // Interface for the dialog listener
    interface DialogListener {
        fun onPositiveClick(roomPosition: Int, password: String)
    }
}



