package com.example.firebaseforum

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.example.firebaseforum.data.UserPhotos
import com.example.firebaseforum.databinding.FragmentUserProfileBinding
import com.example.firebaseforum.firebase.FirebaseHandler
import org.w3c.dom.Text


class UserProfileRecyclerViewAdapter(
    private val values: List<UserPhotos>
) : RecyclerView.Adapter<UserProfileRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentUserProfileBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        if(position != 0)
            holder.nickname.visibility = View.GONE
        else {
            FirebaseHandler.RealtimeDatabase.getOtherUserNicknameRef(item.userID).get().addOnSuccessListener {
                holder.nickname.text = it.value.toString()
            }
        }
        FirebaseHandler.RealtimeDatabase.getPhotoDescriptionRef(item.photoID).get().addOnSuccessListener {
            holder.description.text = it.value.toString()
        }
        FirebaseHandler.RealtimeDatabase.getImage(item.photoID).getBytes(4196*4196).addOnSuccessListener{
            val image = it.toBitmap()
            holder.image.setImageBitmap(image)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentUserProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val image: ImageView = binding.photo
        val nickname: TextView = binding.profileNickname
        val description: TextView = binding.profDescText
        val container: View = binding.root

        override fun toString(): String {
            return super.toString() + " '" + nickname.text + "'"
        }
    }

    private fun ByteArray.toBitmap(): Bitmap {
        return BitmapFactory.decodeByteArray(this, 0, this.size)
    }

}