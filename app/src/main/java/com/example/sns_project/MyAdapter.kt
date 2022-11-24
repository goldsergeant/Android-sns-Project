package com.example.sns_project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sns_project.databinding.ListfrienditemBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.ktx.Firebase

data class Item( val name: String, val email: String) {
    constructor(doc: QueryDocumentSnapshot) :
            this(doc.id, doc["email"].toString())
    constructor(key: String, map: Map<*, *>) :
            this(key,map["email"].toString())
}

class MyViewHolder(val binding: ListfrienditemBinding) : RecyclerView.ViewHolder(binding.root)

class MyAdapter(private val context: ListFriend, private var items: List<Item>)
    : RecyclerView.Adapter<MyViewHolder>() {
    val database= Firebase.database
    val friendsRef= Firebase.auth.currentUser?.let { database.getReference("friends").child(it.uid) }

    fun updateList(newList: List<Item>) {
        items = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ListfrienditemBinding = ListfrienditemBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.binding.friendListName.text = item.name
        holder.binding.friendListEmail.text=item.email

        holder.binding.deleteFriendButton.setOnClickListener {
            deleteItem(position)
        }

    }

    override fun getItemCount() = items.size

    private fun deleteItem(position: Int) {
        val item=items[position]
        friendsRef?.child(item.name)?.removeValue()?.addOnSuccessListener {  }
    }
}
