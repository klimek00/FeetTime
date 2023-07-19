package com.example.firebaseforum.ui.forums

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseforum.R
import com.example.firebaseforum.data.User
import com.example.firebaseforum.databinding.ForumsScreenItemBinding
import com.example.firebaseforum.firebase.FirebaseHandler
import com.example.firebaseforum.firebase.FirebaseHandler.RealtimeDatabase.getImage
import com.example.firebaseforum.firebase.FirebaseHandler.RealtimeDatabase.getOtherUserElement
import com.example.firebaseforum.firebase.FirebaseHandler.RealtimeDatabase
import com.example.firebaseforum.firebase.FirebaseHandler.RealtimeDatabase.getOtherUserUID
import com.example.firebaseforum.helpers.RVItemClickListener
import com.example.firebaseforum.helpers.myCapitalize


// The class takes an RVItemClickListener as a parameter, which will be used to handle clicks on the RecyclerView items
class ForumsRecyclerViewAdapter(private val clickListener: RVItemClickListener) :
    ListAdapter<User, ForumsRecyclerViewAdapter.ViewHolder>(Comparator) {

    fun ByteArray.toBitmap(): Bitmap {
        return BitmapFactory.decodeByteArray(this, 0, this.size)
    }

    // A companion object with an implementation of DiffUtil's ItemCallback to compare Room objects
    object Comparator : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.nickname == newItem.nickname
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
                getImage(user.uid.toString()).getBytes(4196*4196).addOnSuccessListener {
                    Log.d("userid:", user.uid.toString())
                    val img = it.toBitmap()
                    profileImage.setImageBitmap(img)
                }
            }


            //get username email, if the same -> unlock his own profile
            Log.d("isowner: ", user.uid!!)

            /**
             * TODO: czemu to jakies gowno zwraca
             */
            val isOwner = getOtherUserElement(user.uid!!,"email")
            Log.d("ownermeial: ", isOwner.toString())

            // Sets the lock icon to either the open or closed version based on the Room's isPrivate field
//            lock.setImageResource(
//                if (room.isPrivate == true)
//                    R.drawable.ic_lock
//                else
//                    R.drawable.ic_lock_open
//            )
            // Sets the background color of the decoration view based on whether the user is the owner of the Room
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


