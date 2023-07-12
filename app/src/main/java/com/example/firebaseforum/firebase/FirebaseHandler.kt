package com.example.firebaseforum.firebase

import android.net.Uri
import android.util.Log
import com.example.firebaseforum.data.Post
import com.example.firebaseforum.data.Room
import com.example.firebaseforum.data.User
import com.example.firebaseforum.data.Image
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*


object FirebaseHandler {

  object RealtimeDatabase {
    // Set constants for paths to the different database objects
    private const val roomsPath: String = "rooms"
    private const val messagesPath: String = "messages"
    private const val usersPath: String = "users"
    private const val roomLastMessagePath = "lastMessage"
    private const val roomLastMessageAuthorPath = "lastMessageAuthor"
    private const val roomLastMessageTimestampPath = "lastMessageTimestamp"
    private const val imagesPath: String = "images"

    // Initialize a Firebase database instance using the lazy initialization pattern
    private val firebaseDatabase by lazy {
      Firebase.database("https://feettime-c0e86-default-rtdb.europe-west1.firebasedatabase.app//")
    }

     fun getImageStorageRef(filename: String): StorageReference {
      return FirebaseStorage.getInstance().getReference("$imagesPath/$filename")
    }

    private fun getImageDatabaseRef(): DatabaseReference {
      return firebaseDatabase.reference.child(imagesPath)
    }

    fun addImage(image: Image, uri: Uri?) {
//      getImageStorageRef(image.filename!!).putFile(uri!!)
      uploadImage(image.filename!!, uri!!)

      getImageDatabaseRef().child(image.filename).setValue(image)
    }

    fun uploadImage(filename: String, uri: Uri?) {
      getImageStorageRef(filename).putFile(uri!!)
    }
    //get reference to the  "users" obj in DB
    private fun getUsersReference(): DatabaseReference {
      return firebaseDatabase.reference.child(usersPath)
    }
    //function to add new user to DB
    fun addUser(user: User) {
      val userUid = Authentication.getUserUid()
      userUid?.let {
        val userReference = getUsersReference().child(userUid)
        userReference.setValue(user)
      }
    }

    fun addUserDescription(description: String){
      val userUid = Authentication.getUserUid()
      userUid?.let {
        val userRef = getUsersReference().child(userUid).child("description")
        userRef.setValue(description)
      }
    }

    fun addUserNickName(nickname: String){
      val userUid = Authentication.getUserUid()
      userUid?.let {
        val userRef = getUsersReference().child(userUid).child("nickname")
        userRef.setValue(nickname)

      }
    }

    fun getDescriptionRef(): DatabaseReference{
      var userRef: DatabaseReference? = null
      val userUid = Authentication.getUserUid()
      userUid?.let {
        userRef = getUsersReference().child(userUid).child("description")
      }
      return userRef!!
    }

    fun getNicknameRef(): DatabaseReference{
      var nicknameRef: DatabaseReference? = null
      val userUid = Authentication.getUserUid()
      userUid?.let {
        nicknameRef = getUsersReference().child(userUid).child("nickname")
      }
      return nicknameRef!!
    }

    private fun getRoomsReference(): DatabaseReference {
      return firebaseDatabase.reference.child(roomsPath)
    }

    fun addRoom(room: Room) {
      Log.d("addRoom", getRoomsReference()::class.java.typeName)
      getRoomsReference().child(room.roomName!!).setValue(room)
    }
    fun addUserRooms(roomName: String) {
      val userUid = Authentication.getUserUid()
      Log.d("addUserRooms", getRoomsReference()::class.java.typeName)
      userUid?.let {
        Log.d("addUserRooms uid", userUid)
        Log.d("addUserRooms roomName", roomName)
        getUsersReference().child(userUid).child(roomName).setValue(true)
      }
    }

    fun getRooms(): Task<DataSnapshot> {
      return getRoomsReference().get()
    }

    //getRoomsReference - path to room
    fun listenToRoomsReference(listener: ChildEventListener) {
      getRoomsReference().addChildEventListener(listener)
    }

    fun stopListeningToRoomsRef(listener: ChildEventListener) {
      getRoomsReference().removeEventListener(listener)
    }

    ///
      // MESSAGES
    ///
    private fun getMessagesReference(room: String): DatabaseReference {
      return firebaseDatabase.reference.child(messagesPath).child(room)
    }

    // Function to attach a ValueEventListener to a specific room's messages in the "messages" object
// in the database
    fun listenToMessageFromRoom(room: String, listener: ValueEventListener) {
      getMessagesReference(room).addValueEventListener(listener)
    }

    // This function updates the "last message" information for a given room.
    // It takes the room name and a Post object as parameters
    private fun updateRoomLastMessage(roomName: String, post: Post) {
      getRoomsReference().child(roomName).apply {
        // Set the "last message" child nodes to the values of the corresponding
        // properties of the Post object.
        updateChildren(mapOf<String, Any>(
          roomLastMessagePath to post.message!!,
          roomLastMessageAuthorPath to post.author!!,
          roomLastMessageTimestampPath to post.timestamp!!
        ))
      }
    }

    fun addMessage(roomName: String, post: Post) {
      getMessagesReference(roomName).child(post.timestamp!!.toString()).setValue(post)
      updateRoomLastMessage(roomName, post)
    }

    fun stopListeningToRoomMessages(roomName: String, listener: ValueEventListener) {
      getMessagesReference(roomName).removeEventListener(listener)
    }

    //
  }

  object Authentication {
    // Create a FirebaseAuth instance with a Firebase.auth
    private val firebaseAuth by lazy { Firebase.auth }
    // Login function using email and password credentials
    // It returns a Task<AuthResult> for asynchronous handling
    fun login(email: String, password: String): Task<AuthResult> {
      // Login process:
      // To sign in the app we use a signInWithEmailAndPassword method from the FirebaseAuth
      // variable and an OnCompleteListener to handle the result.
      // NOTE: This method of authentication has to be enabled in the project
      return firebaseAuth.signInWithEmailAndPassword(email, password)
    }
    // Register function using email and password credentials
    // It returns a Task<AuthResult> for asynchronous handling
    fun register(email: String, password: String): Task<AuthResult> {
      // register process:
      // To sign up in the app we use a createUserWithEmailAndPassword method from the FirebaseAuth
      // variable and an OnCompleteListener to handle the result.
      // NOTE: This method of authentication has to be enabled in the project
      return firebaseAuth.createUserWithEmailAndPassword(email, password)
    }


    // Get the email address of the current user
    fun getUserEmail():String?{
      return firebaseAuth.currentUser?.email
    }
    // Get the UID of the current user
    fun getUserUid():String?{
      return firebaseAuth.currentUser?.uid
    }
    // Check if there is a current user logged in
    fun isLoggedIn(): Boolean {
      return firebaseAuth.currentUser != null
    }
    // Log out the current user
    fun logout() {
      firebaseAuth.signOut()
    }
  }
}