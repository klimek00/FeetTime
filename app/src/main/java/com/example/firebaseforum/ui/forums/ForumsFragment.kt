package com.example.firebaseforum.ui.forums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseforum.R
import com.example.firebaseforum.data.Room
import com.example.firebaseforum.databinding.FragmentForumsBinding
import com.example.firebaseforum.firebase.FirebaseHandler
import com.example.firebaseforum.helpers.RVItemClickListener
import com.example.firebaseforum.ui.dialogs.AddRoomDialog
import com.example.firebaseforum.ui.dialogs.RoomPasswordDialog
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
  private var rooms: ArrayList<Room> = ArrayList()
  private val invalidRoomNames: ArrayList<String> = ArrayList()


  private val addDialogListener = object : AddRoomDialog.DialogListener {
    override fun onPositiveClick(roomName: String, password: String) {
      // Creates a new Room object with the provided roomName,
      // current user's uid, email, empty message fields,
      // current timestamp, and password flag and value.
      val newRoom = Room(
        roomName,
        FirebaseHandler.Authentication.getUserUid(),
        FirebaseHandler.Authentication.getUserEmail(),
        "",
        "",
        System.currentTimeMillis(),
        password.isNotEmpty(),
        password
      )
// Adds the newly created Room object to the database using the
// FirebaseHandler.RealtimeDatabase.addRoom method.
      FirebaseHandler.RealtimeDatabase.addRoom(newRoom)
// Adds the roomName to the user's rooms list in the Realtime Database.
      FirebaseHandler.RealtimeDatabase.addUserRooms(roomName)
    }
  }

  private val passwordDialogListener = object: RoomPasswordDialog.DialogListener {
    override fun onPositiveClick(roomPosition: Int, password: String) {
      if (roomPosition != -1) {
        val room = rooms[roomPosition]
        room.roomPassword?.let { roomPassword ->
          if (roomPassword == password) {
            val navigateToRoomFragmentAction = ForumsFragmentDirections.actionNavigationForumsToRoomFragment(room.roomName!!)
            findNavController().navigate(navigateToRoomFragmentAction)
            // if wrong, retry
          } else {
            Snackbar.make(
              binding.forumList,
              getString(R.string.wrong_pass_msg),
              Snackbar.LENGTH_LONG
            ).setAction(getString(R.string.try_again_msg)) {
              showPasswordDialog(roomPosition)
            }.show()
          }
        }
      }
    }
  }

  /**
   * Shows a password dialog for a private chat room.
   * @param roomPosition The index of the room in the list.
   */
  private fun showPasswordDialog(roomPosition: Int) {
    // Create a new instance of the password dialog.
    val newInstance = RoomPasswordDialog().newInstance()

    // Set the room position and dialog listener for the new instance.
    newInstance.apply {
      setRoomPosition(roomPosition)
      setDialogListener(passwordDialogListener)
    }

    // Show the password dialog using the activity's fragment manager.
    newInstance.show(requireActivity().supportFragmentManager, "PasswordDialog")
  }


    private val listItemClickListener: RVItemClickListener = object : RVItemClickListener {
    override fun onItemClick(position: Int) {
      val room = rooms[position]
      room.roomName?.let {
        if (room.isPrivate == false ||
            room.ownerEmail == FirebaseHandler.Authentication.getUserEmail()) {
          val navigateToRoomFragmentAction = ForumsFragmentDirections.actionNavigationForumsToRoomFragment(it)
          findNavController().navigate(navigateToRoomFragmentAction)
        } else {
          showPasswordDialog(position)
        }
      }
    }
  }


  //shows dialog for adding a new room to the forum
  private fun showAddDialog() {
    val newInstance = AddRoomDialog().newInstance()
    newInstance.apply {
      setDialogListener(addDialogListener)
      setInvalidNames(invalidRoomNames)
    }

    newInstance.show(requireActivity().supportFragmentManager, "AddRoomDialog")
  }

  //sets up the button listeners for the fragment
  private fun setupButtons() {
    binding.addForumFab.setOnClickListener {
      showAddDialog()
    }
  }

  private fun setupRecyclerView() {
    //create adapter with onclicklistener
    listAdapter = ForumsRecyclerViewAdapter(listItemClickListener)
    with(binding.forumList) {
      layoutManager = LinearLayoutManager(requireContext())
      adapter = listAdapter
    }
  }

  private fun addRoom(room: Room, isFirst: Boolean = false): Int {
    var idx = 0

    // If the new room is not the first room in the list, find the appropriate index for it based
    // on its timestamp.
    // The newest items (with higher timestamp) are at the front of the list
    if (!isFirst) {
      for ((i, existingRoom) in rooms.withIndex()) {
        if (room.lastMessageTimestamp!! >= existingRoom.lastMessageTimestamp!!) {
          idx = i
          break
        } else {
          idx = i + 1
        }
      }
    }
    invalidRoomNames.add(idx, room.roomName!!)
    rooms.add(idx, room)

    return idx
  }

  private fun showList(rooms: List<Room>, position: Int = -1) {
    binding.forumList.visibility = View.INVISIBLE

    binding.root.postDelayed({
      binding.forumList.visibility = View.VISIBLE
      val animation: LayoutAnimationController = AnimationUtils.loadLayoutAnimation(
        requireContext(), R.anim.layout_animation_fall_down
      )
      binding.forumList.layoutAnimation = animation
      binding.forumList.scheduleLayoutAnimation()
      listAdapter.submitList(rooms)

      listAdapter.submitList(rooms)
      if (position != -1) {
        listAdapter.notifyDataSetChanged()
      }

      binding.forumList.smoothScrollToPosition(0)
    }, 50)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.root.postDelayed({
//            isFirstGet = true
      setupButtons()
      setupRecyclerView()

      FirebaseHandler.RealtimeDatabase.getRooms().addOnSuccessListener {
        for (child in it.children) {
          val roomFromDB = child.getValue<Room>()
          roomFromDB?.let {
            addRoom(roomFromDB)
          }
        }
        showList(rooms)
        FirebaseHandler.RealtimeDatabase.listenToRoomsReference(this@ForumsFragment)
      }

    }, 100)

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
    rooms.clear()
    invalidRoomNames.clear()
    FirebaseHandler.RealtimeDatabase.stopListeningToRoomsRef(this)

    // Set the binding object to null to avoid memory leaks.
    _binding = null
  }

  
  override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
    if (snapshot.value != null && !invalidRoomNames.contains(snapshot.key)) {
      val lastRoom = snapshot.getValue<Room>()
      lastRoom?.let {
        val roomPos = addRoom(lastRoom, true)
        showList(rooms, roomPos)
      }
    }
  }

  override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
    if (snapshot.value != null) {
      val changedRoom = snapshot.getValue<Room>()
      changedRoom?.let {
        val roomPos = invalidRoomNames.indexOf(changedRoom.roomName)
        invalidRoomNames.removeAt(roomPos)
        rooms.removeAt(roomPos)
        addRoom(changedRoom, true)
        val newRoomPos = rooms.indexOf(changedRoom)
        showList(rooms, newRoomPos)
      }
    }
  }

  override fun onChildRemoved(snapshot: DataSnapshot) {
    TODO("Not yet implemented")
  }

  override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
    TODO("Not yet implemented")
  }

  override fun onCancelled(error: DatabaseError) {
    TODO("Not yet implemented")
  }


}

