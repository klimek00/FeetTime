package com.example.firebaseforum.ui.forums

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseforum.R
import com.example.firebaseforum.data.Room
import com.example.firebaseforum.databinding.ForumsScreenItemBinding
import com.example.firebaseforum.firebase.FirebaseHandler
import com.example.firebaseforum.helpers.RVItemClickListener
import com.example.firebaseforum.helpers.myCapitalize
import com.example.firebaseforum.helpers.toDateString


// The class takes an RVItemClickListener as a parameter, which will be used to handle clicks on the RecyclerView items
class ForumsRecyclerViewAdapter(private val clickListener: RVItemClickListener) :
    ListAdapter<Room, ForumsRecyclerViewAdapter.ViewHolder>(Comparator) {

    // A companion object with an implementation of DiffUtil's ItemCallback to compare Room objects
    object Comparator : DiffUtil.ItemCallback<Room>() {
        override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
            // Returns true if the roomName and lastMessageTimestamp fields of the Room objects are the same
            return oldItem.roomName == newItem.roomName && oldItem.lastMessageTimestamp == newItem.lastMessageTimestamp
        }
    }

    // Creates a ViewHolder for the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflates the view from the ForumsScreenItemBinding and returns a new instance of ViewHolder
        return ViewHolder(
            ForumsScreenItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    // Binds the ViewHolder to the item at the specified position
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        //calls the bind method of viewHolder, passing in the room object at the specificied position
        holder.bind(item)
        //sets on item click listener on the viewholder's root view, calling onItemClick with the adapter position
        holder.setOnClickListener(clickListener)
    }



    // ViewHolder class for the RecyclerView
    inner class ViewHolder(binding: ForumsScreenItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Views in the ViewHolder layout
        private val forumLabel: TextView = binding.forumLabel
        private val forumOwner: TextView = binding.forumOwner
        private val forumDate: TextView = binding.forumDate
        private val postAuthor: TextView = binding.postAuthor
        private val lock: ImageView = binding.lock
        private val decoration: View = binding.decoration
        private val rootView = binding.root


        override fun toString(): String {
            // Returns a string representation of the ViewHolder
            return super.toString() + " '" + (forumLabel.text) + "'"
        }
        fun setOnClickListener(listener: RVItemClickListener) {
            rootView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
        fun bind(room: Room) {
            forumLabel.text = room.roomName?.myCapitalize()
            forumOwner.text = "by ${room.ownerEmail}"
            forumDate.text = room.lastMessageTimestamp?.toDateString()
            postAuthor.text = room.lastMessageAuthor
            val isOwner = room.ownerEmail == FirebaseHandler.Authentication.getUserEmail()
            // Sets the lock icon to either the open or closed version based on the Room's isPrivate field
            lock.setImageResource(
                if (room.isPrivate == true)
                    R.drawable.ic_lock
                else
                    R.drawable.ic_lock_open
            )
            // Sets the background color of the decoration view based on whether the user is the owner of the Room
                decoration.setBackgroundColor(
                    decoration.context.getColor(
                        if (isOwner)
                            R.color.secondary
                        else
                            R.color.primary
                    )
                )
        }
    }
}


