package com.example.firebaseforum.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.firebaseforum.R
import com.example.firebaseforum.databinding.DialogAddRoomBinding


// Define a class called AddRoomDialog that extends DialogFragment
class AddRoomDialog : DialogFragment() {

  // Declare some private properties
  private lateinit var binding: DialogAddRoomBinding // Lazily initialize the binding variable later

  //define a listener obj that will receive events from the dialog
  private var mListener: DialogListener? = null
  private var invalidNames = ArrayList<String>()

  // Define a factory method for creating instances of the dialog
  fun newInstance(): AddRoomDialog {
    return AddRoomDialog()
  }

  fun setDialogListener(listener: DialogListener) {
    mListener = listener
  }

  fun setInvalidNames(list: ArrayList<String>) {
    invalidNames = list
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

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    binding.dialogPositiveButton.setOnClickListener {
      val roomName = binding.roomName.editText?.text?.toString()?.trim()?.lowercase()
      val password = binding.roomPassword.editText?.text?.toString()

      if (roomName?.isNotEmpty() == true) {
        if (invalidNames.isNotEmpty() && invalidNames.contains(roomName)) {
          binding.roomName.error = getString(R.string.name_taken)
        } else {
          //check if room is private and if so, if a password was entered
          if (!binding.privateSwitch.isChecked ||
            (binding.privateSwitch.isChecked && password?.isNotEmpty() == true)) {
            //call onPositiveClick method of the dialog listener with the room name and password (if any)
            binding.roomName.error = ""
            mListener?.onPositiveClick(roomName, password!!)
            dismiss()
          } else {
            //password required but not entered
            binding.roomPassword.error = getString(R.string.missing_input)
          }
        }
      } else {
        //roomName missing
        binding.roomName.error = getString(R.string.missing_input)
      }
    }

    binding.dialogNegativeButton.setOnClickListener {
      dismiss()
    }

    //listener for private switch in dialog, toggle visibility of password field based on state of switch
    binding.privateSwitch.setOnCheckedChangeListener { _, state ->
      binding.roomPassword.visibility = if (state) View.VISIBLE else View.GONE
    }

    super.onViewCreated(view, savedInstanceState)
  }


  // Define an interface for the dialog listener
  interface DialogListener {
    fun onPositiveClick(roomName: String, password: String)
  }
}






