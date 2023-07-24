package com.example.firebaseforum.data

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.bumptech.glide.disklrucache.DiskLruCache.Value
import com.example.firebaseforum.firebase.FirebaseHandler
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

object Photos {

    val ITEMS: MutableList<UserPhotos> = ArrayList()
    var iterator: Int = 0

    private val COUNT = 10

    fun addItem(item: UserPhotos) {
        ITEMS.add(item)
    }

    fun clearData(){
        ITEMS.clear()
        iterator = 0
    }

    fun getNextElement(): String? {
        var photoID: String? = null
        photoID = ITEMS[iterator].photoID
        iterator += 1
        return photoID
    }

    fun ableToDownload(): Boolean{
        return iterator < ITEMS.size
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