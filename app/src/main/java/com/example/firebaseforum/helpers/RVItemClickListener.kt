package com.example.firebaseforum.helpers

// Interface definition for an item click listener in a RecyclerView
interface RVItemClickListener {
    // This method is called when an item in the RecyclerView is clicked
    // It takes in the position of the clicked item as an argument
    fun onItemClick(position:Int)
}
