package com.example.firebaseforum.ui.forums

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseforum.R
import com.example.firebaseforum.data.Post
import com.example.firebaseforum.databinding.FragmentRoomBinding
import com.example.firebaseforum.firebase.FirebaseHandler
import com.example.firebaseforum.helpers.KeyboardHelper
import com.example.firebaseforum.helpers.MyTextWatcher
import com.example.firebaseforum.helpers.TimerTaskListener
import com.example.firebaseforum.helpers.myCapitalize
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class RoomFragment : Fragment(), ValueEventListener {
  private var _binding: FragmentRoomBinding? = null
  private val args: RoomFragmentArgs by navArgs()

  private lateinit var listAdapter: RoomRecyclerViewAdapter
  private lateinit var roomName: String
  private val posts: ArrayList<Post> = ArrayList()
  private var isFirstGet: Boolean = true

  private val messageTextWatcher: MyTextWatcher = MyTextWatcher(object: TimerTaskListener {
    override fun timerRun() {
      requireActivity().runOnUiThread {
        val message = binding.message.editText?.text.toString()
        val notEmpty  = message.isNotEmpty()

        binding.postSendButton.isEnabled = notEmpty
        val tintColor = binding.root.context.getColor(
          if (notEmpty) R.color.secondary else R.color.grey
        )
        binding.postSendButton.backgroundTintList = ColorStateList.valueOf(tintColor)
      }
    }
  })

  private fun setupButtons() {
    binding.postSendButton.isEnabled = false
    binding.postSendButton.setOnClickListener {
      messageTextWatcher.cancelTimer()
      val message = binding.message.editText?.text.toString()
      FirebaseHandler.RealtimeDatabase.addMessage(
        roomName, Post(
          FirebaseHandler.Authentication.getUserEmail(),
          message,
          System.currentTimeMillis()
        )
      )

      binding.message.editText?.text?.clear()
      KeyboardHelper.hideSoftwareKeyboard(requireContext(), binding.root.windowToken)
    }
  }

  private fun setupEditText() {
    binding.message.editText?.addTextChangedListener(messageTextWatcher)
  }

  private fun setupRecyclerView() {
    listAdapter = RoomRecyclerViewAdapter()
    with (binding.messageList) {
      layoutManager = LinearLayoutManager(requireContext())
      adapter = listAdapter
    }
  }

  private fun showList(posts: List<Post>) {
    binding.messageList.visibility = View.VISIBLE
    listAdapter.submitList(posts)
    binding.messageList.scrollToPosition(posts.size -1)
    if (!isFirstGet) {
      // notify the adapter that an item has been inserted if it's not the first time
      listAdapter.notifyItemInserted(posts.size)
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    isFirstGet = true
    roomName = args.roomName
    //set the toolbar title
    (requireActivity() as AppCompatActivity).supportActionBar?.title = roomName.myCapitalize()

    //listen for messages from DB
    FirebaseHandler.RealtimeDatabase.listenToMessageFromRoom(roomName, this)
    setupButtons()
    setupEditText()
    setupRecyclerView()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentRoomBinding.inflate(inflater, container, false)
    return binding.root
  }

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
    posts.clear()
    FirebaseHandler.RealtimeDatabase.stopListeningToRoomMessages(roomName, this)
  }

  override fun onResume() {
    super.onResume()
    setupEditText()
  }

  override fun onPause() {
    super.onPause()
    binding.message.editText?.removeTextChangedListener(messageTextWatcher)
    messageTextWatcher.cancelTimer()
  }

  //called when data in the realtime DB changes
  override fun onDataChange(snapshot: DataSnapshot) {
    if (snapshot.value != null) {
      if (isFirstGet) {
        for (child in snapshot.children) {
          val post = child.getValue<Post>()
          post?.let {
            posts.add(post)
          }
        }
      } else {
        val lastPost = snapshot.children.lastOrNull()?.getValue<Post>()
        lastPost?.let {
          posts.add(lastPost)
        }
      }
      showList(posts)
      isFirstGet = false
    }
  }

  override fun onCancelled(error: DatabaseError) {
  }

}

