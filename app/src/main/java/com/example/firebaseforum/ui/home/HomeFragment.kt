package com.example.firebaseforum.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.example.firebaseforum.R
import com.example.firebaseforum.data.Room
import com.example.firebaseforum.data.User
import com.example.firebaseforum.databinding.FragmentHomeBinding
import com.example.firebaseforum.firebase.FirebaseHandler
import com.example.firebaseforum.firebase.FirebaseHandler.RealtimeDatabase.addRoom
import com.example.firebaseforum.firebase.FirebaseHandler.RealtimeDatabase.getImage
import com.example.firebaseforum.firebase.FirebaseHandler.RealtimeDatabase.getImageStorageRef
import com.example.firebaseforum.helpers.RVItemClickListener
import com.example.firebaseforum.ui.forums.ForumsFragmentDirections
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.getValue


class HomeFragment : Fragment(), ChildEventListener {
    private var _binding: FragmentHomeBinding? = null
    private lateinit var listAdapter: HomeRecyclerViewAdapter
    private var users: ArrayList<User> = ArrayList()
//    private val invalidRoomNames: ArrayList<String> = ArrayList()
    private lateinit var userUID: String

//    fun ByteArray.toBitmap(): Bitmap {
//        return BitmapFactory.decodeByteArray(this, 0, this.size)
//    }

    private fun addUser(user: User, isFirst: Boolean = false): Int {
        var idx = 0

        if (!isFirst) {
            for ((i, existingUser) in users.withIndex()) {
                idx = i + 1
            }
        }
        users.add(idx, user)

        return idx
    }

    private val listItemClickListener: RVItemClickListener = object : RVItemClickListener {
        override fun onItemClick(position: Int) {
            val user = users[position]
            user.nickname?.let {
                val action = HomeFragmentDirections.actionNavigationHomeToUserProfileFragment(user.uid!!)
                findNavController().navigate(action)
            }
        }
    }

    private fun setupRecyclerView() {
        //create adapter with onclicklistener
        listAdapter = HomeRecyclerViewAdapter(listItemClickListener)
        with(binding.homeList) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
        }
    }
    private fun showList(users: List<User>, position: Int = -1) {
        binding.homeList.visibility = View.INVISIBLE

        binding.root.postDelayed({
            binding.homeList.visibility = View.VISIBLE
            val animation: LayoutAnimationController = AnimationUtils.loadLayoutAnimation(
                requireContext(), R.anim.layout_animation_fall_down
            )
            binding.homeList.layoutAnimation = animation
            binding.homeList.scheduleLayoutAnimation()

            listAdapter.submitList(users)
            if (position != -1) {
                listAdapter.notifyDataSetChanged()
            }

            binding.homeList.smoothScrollToPosition(0)
        }, 50)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.postDelayed({
            if (!FirebaseHandler.Authentication.isLoggedIn()) {
                return@postDelayed
            }
//            isFirstGet = true
//            setupButtons()
            setupRecyclerView()

            FirebaseHandler.RealtimeDatabase.getUsers().addOnSuccessListener {
                for (child in it.children) {
                    val userFromDB = child.getValue<User>()?.apply { uid = child.key} ?: break
                    Log.d("tagggg:", "${userFromDB?.nickname}, ${FirebaseHandler.Authentication.getUserEmail()}, userid: $userFromDB, child: ${userFromDB.uid}")

                    userFromDB?.let {
                        addUser(userFromDB)
                    }
                }
                showList(users)
                FirebaseHandler.RealtimeDatabase.listenToUsersReference(this@HomeFragment)
            }

        }, 100)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the view using the generated binding class and set the _binding property.
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val binding get() = _binding!!

    // This method is called when the view is destroyed.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//        if (snapshot.value != null && !invalidRoomNames.contains(snapshot.key)) {
//            val lastRoom = snapshot.getValue<Room>()
//            if (lastRoom?.ownerEmail == FirebaseHandler.Authentication.getUserEmail()) {
//                lastRoom?.let {
//                    val roomPos = addRoom(lastRoom, true)
//                    showList(rooms, roomPos)
//                }
//            }
//        }
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//        if (snapshot.value != null) {
//            val changedRoom = snapshot.getValue<Room>()
//            if (changedRoom?.ownerEmail == FirebaseHandler.Authentication.getUserEmail()) {
//                changedRoom?.let {
//                    val roomPos = invalidRoomNames.indexOf(changedRoom.roomName)
//                    invalidRoomNames.removeAt(roomPos)
//                    rooms.removeAt(roomPos)
//                    addRoom(changedRoom, true)
//                    val newRoomPos = rooms.indexOf(changedRoom)
//                    showList(rooms, newRoomPos)
//                }
//            }
//        }
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
//        TODO("Not yet implemented")
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//        TODO("Not yet implemented")
    }

    override fun onCancelled(error: DatabaseError) {
//        TODO("Not yet implemented")
    }

}
