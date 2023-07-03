package com.example.firebaseforum.ui.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.firebaseforum.R
import com.example.firebaseforum.databinding.DialogRoomPasswordBinding

class RoomPasswordDialog : DialogFragment() {
    // Declare variables for view binding, dialog listener, and room position
    private lateinit var binding: DialogRoomPasswordBinding
    private var mListener: DialogListener? = null
    private var mRoomPosition: Int = -1

    fun setRoomPosition(roomPosition: Int) {
        mRoomPosition = roomPosition
    }
    // Create a new instance of the RoomPasswordDialog fragment
    fun newInstance(): RoomPasswordDialog {
        return RoomPasswordDialog()
    }


    fun setDialogListener(listener: DialogListener) {
        mListener = listener
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.dialogPositiveButton.setOnClickListener {
            val password = binding.roomPassword.editText?.text?.toString()
            if (password?.isNotEmpty() == true) {
                // call the positive click listener with the room position and password, then dismiss dialog
                mListener?.onPositiveClick(mRoomPosition, password)
                dismiss()
            } else {
                binding.roomPassword.error = getString(R.string.missing_input)
            }
        }
        binding.dialogNegativeButton.setOnClickListener {
            dismiss()
        }
    }


    // Interface for the dialog listener
    interface DialogListener {
        fun onPositiveClick(roomPosition: Int, password: String)
    }
}



