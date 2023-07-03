package com.example.firebaseforum.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseforum.R
import com.example.firebaseforum.data.Room
import com.example.firebaseforum.databinding.HomeScreenItemBinding
import com.example.firebaseforum.firebase.FirebaseHandler
import com.example.firebaseforum.helpers.RVItemClickListener
import com.example.firebaseforum.helpers.myCapitalize
import com.example.firebaseforum.helpers.toDateString
import com.example.firebaseforum.ui.forums.ForumsRecyclerViewAdapter


class HomeRecyclerViewAdapter(
    private val clickListener: RVItemClickListener // Constructor that takes an RVItemClickListener object as a parameter
) : ListAdapter<Room, HomeRecyclerViewAdapter.ViewHolder>(Comparator) { // Extends ListAdapter, with Room as the data type and ViewHolder as the view holder class

    object Comparator :
        DiffUtil.ItemCallback<Room>() {
        // Defines a Comparator object that extends DiffUtil.ItemCallback and specifies the data type (Room)

        override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
            // Checks if the items are the same object (i.e., have the same reference)
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
            // Compares the contents of the items to check if they're the same
            return oldItem.roomName == newItem.roomName && oldItem.lastMessageTimestamp == newItem.lastMessageTimestamp
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        // Overrides the onCreateViewHolder method to inflate the layout and create a ViewHolder
        return ViewHolder(
            HomeScreenItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    // Overrides the onBindViewHolder method to bind the data to the ViewHolder and set the click listener
    override fun onBindViewHolder(holder: HomeRecyclerViewAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        //calls the bind method of viewHolder, passing in the room object at the specificied position
        holder.bind(item)
        //sets on item click listener on the viewholder's root view, calling onItemClick with the adapter position
        holder.setOnClickListener(clickListener)
    }

    // Defines a ViewHolder that extends RecyclerView.ViewHolder and takes a binding object as a parameter
    inner class ViewHolder(binding: HomeScreenItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // Initializes the TextViews and Views with their respective views in the layout
        private val itemLabel: TextView = binding.homeItemLabel
        private val itemDate: TextView = binding.date
        private val itemPost: TextView = binding.post
        private val decoration: View = binding.decoration
        private val rootView = binding.root


        override fun toString(): String {
            return super.toString() + " '" + (itemLabel.text) + "'"
        }
        fun setOnClickListener(listener: RVItemClickListener) {
            rootView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
        fun bind(room: Room) {
            itemLabel.text = room.ownerEmail
            itemDate.text = room.lastMessageTimestamp?.toDateString()
            itemPost.text = room.lastMessage
            val isOwner = room.ownerEmail == FirebaseHandler.Authentication.getUserEmail()

        }
    }

}


