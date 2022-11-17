package com.example.sns_project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sns_project.databinding.ListfrienditemBinding
import com.google.firebase.firestore.QueryDocumentSnapshot

data class Item( val name: String, val email: String) {
    constructor(doc: QueryDocumentSnapshot) :
            this(doc.id, doc["email"].toString())
    constructor(key: String, map: Map<*, *>) :
            this(key,map["email"].toString())
}

class MyViewHolder(val binding: ListfrienditemBinding) : RecyclerView.ViewHolder(binding.root)

class MyAdapter(private val context: ListFriend, private var items: List<Item>)
    : RecyclerView.Adapter<MyViewHolder>() {

    fun interface OnItemClickListener {
        fun onItemClick(student_id: String)
    }

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

    }

    override fun getItemCount() = items.size
}
