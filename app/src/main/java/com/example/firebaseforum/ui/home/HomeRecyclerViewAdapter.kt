package com.example.firebaseforum.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
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
import com.example.firebaseforum.data.User
import com.example.firebaseforum.databinding.HomeScreenItemBinding
import com.example.firebaseforum.firebase.FirebaseHandler
import com.example.firebaseforum.helpers.RVItemClickListener
import com.example.firebaseforum.helpers.myCapitalize
import com.example.firebaseforum.helpers.toDateString
import com.example.firebaseforum.ui.forums.ForumsRecyclerViewAdapter

/**
 * TODO: homeRV musi być spisem zdjęć jednego uzytkownika
 */
class HomeRecyclerViewAdapter(
    private val clickListener: RVItemClickListener // Constructor that takes an RVItemClickListener object as a parameter
) : ListAdapter<User, HomeRecyclerViewAdapter.ViewHolder>(Comparator) { // Extends ListAdapter, with Room as the data type and ViewHolder as the view holder class

    fun ByteArray.toBitmap(): Bitmap {
        return BitmapFactory.decodeByteArray(this, 0, this.size)
    }
    object Comparator :
        DiffUtil.ItemCallback<User>() {
        // Defines a Comparator object that extends DiffUtil.ItemCallback and specifies the data type (Room)

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            // Checks if the items are the same object (i.e., have the same reference)
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            // Compares the contents of the items to check if they're the same
            return oldItem.nickname == newItem.nickname
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
        private val username: TextView = binding.username
        private val description: TextView = binding.description
        private val decoration: View = binding.decoration
        private val profileImage: ImageView = binding.profileImage
        private val rootView = binding.root




        fun setOnClickListener(listener: RVItemClickListener) {
            rootView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
        fun bind(user: User) {
            username.text = user.nickname?.myCapitalize()
            description.text = user.description

            if (user.uid!!.isNotEmpty()) {
                FirebaseHandler.RealtimeDatabase.getImage(user.uid.toString()).getBytes(4196*4196).addOnSuccessListener {
                    Log.d("userid:", user.uid.toString())
                    val img = it.toBitmap()
                    profileImage.setImageBitmap(img)
                }
            }


            //get username email, if the same -> unlock his own profile
            Log.d("isowner: ", user.uid!!)

            val isOwner = FirebaseHandler.RealtimeDatabase.getOtherUserElement(user.uid!!, "email")
            Log.d("ownermeial: ", isOwner.toString())

            decoration.setBackgroundColor(
                decoration.context.getColor(
                    if ("ye" == FirebaseHandler.Authentication.getUserEmail())
                        R.color.secondary
                    else
                        R.color.primary
                )
            )
        }
    }

}


