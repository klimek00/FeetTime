package com.example.firebaseforum.ui.forums

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseforum.EditProfileFragmentDirections
import com.example.firebaseforum.R
import com.example.firebaseforum.data.Room
import com.example.firebaseforum.data.User
import com.example.firebaseforum.databinding.FragmentForumsBinding
import com.example.firebaseforum.firebase.FirebaseHandler
import com.example.firebaseforum.helpers.RVItemClickListener
import com.example.firebaseforum.ui.dialogs.AddRoomDialog
import com.example.firebaseforum.ui.dialogs.RoomPasswordDialog
import com.example.firebaseforum.ui.home.HomeRecyclerViewAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.getValue


// The ForumsFragment is responsible for displaying the list of available chat rooms.
class ForumsFragment : Fragment(), ChildEventListener {
  // View binding for the fragment
  private var _binding: FragmentForumsBinding? = null
  private lateinit var listAdapter: ForumsRecyclerViewAdapter

  private fun setupRecyclerView() {
    //create adapter with onclicklistener
//    listAdapter = ForumsRecyclerViewAdapter(listItemClickListener)
//    with(binding.forumList) {
//      layoutManager = LinearLayoutManager(requireContext())
//      adapter = listAdapter
//    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val action = ForumsFragmentDirections.actionNavigationForumsToUserProfileFragment(FirebaseHandler.Authentication.getUserUid()!!)
    findNavController().navigate(action)
  }

  // Inflate the layout for the fragment
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentForumsBinding.inflate(inflater, container, false)
    return binding.root
  }

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  // This function is called when the view is destroyed.
  override fun onDestroyView() {
    super.onDestroyView()

    // Set the binding object to null to avoid memory leaks.
    _binding = null
  }

  override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//    TODO("Not yet implemented")
  }

  override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//    TODO("Not yet implemented")
  }

  override fun onChildRemoved(snapshot: DataSnapshot) {
//    TODO("Not yet implemented")
  }

  override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//    TODO("Not yet implemented")
  }

  override fun onCancelled(error: DatabaseError) {
//    TODO("Not yet implemented")
  }

}

