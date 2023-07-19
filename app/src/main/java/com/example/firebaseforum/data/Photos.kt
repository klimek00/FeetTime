package com.example.firebaseforum.data

import android.os.Parcel
import android.os.Parcelable
import com.example.firebaseforum.firebase.FirebaseHandler
import java.util.ArrayList

object Photos {

    val ITEMS: MutableList<UserPhotos> = ArrayList()

    private val COUNT = 10
    private var profile_data_loaded: Boolean = false

    private fun addItem(item: UserPhotos) {
        ITEMS.add(item)
    }

    fun getPhotosData(userID: String){
        FirebaseHandler.RealtimeDatabase.getImagesReference().get().addOnSuccessListener {
            val test = it.value
        }
    }

    fun getLoadingStatus(): Boolean = profile_data_loaded

    fun clearData(){
        ITEMS.clear()
        profile_data_loaded = false
    }
}

data class UserPhotos(val userID: String, val photoID: String): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun toString(): String = photoID
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userID)
        parcel.writeString(photoID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserPhotos> {
        override fun createFromParcel(parcel: Parcel): UserPhotos {
            return UserPhotos(parcel)
        }

        override fun newArray(size: Int): Array<UserPhotos?> {
            return arrayOfNulls(size)
        }
    }
}