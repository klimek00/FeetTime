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
    private val values: List<UserPhotos>,
    private val photos: ArrayList<Bitmap>,
    private val titles: ArrayList<String>,
    private val descriptions: ArrayList<String>,
    private val username: String,
    private val profileDesc: String
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
        if(position == 0) {
            if (username != "loading"){
                holder.nickname.text = username
                holder.description.text = profileDesc
                holder.image.setImageBitmap(photos[position])
            }else {
                holder.nickname.text = username
                holder.description.text = profileDesc
                //holder.image.setImageBitmap(photos[position])
            }
        }
        else {
            holder.image.setImageBitmap(photos[position])
            holder.description.text = descriptions[position]
            holder.nickname.text = titles[position]
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