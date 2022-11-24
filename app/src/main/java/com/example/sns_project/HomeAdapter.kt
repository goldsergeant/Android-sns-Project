package com.example.sns_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.homeitem.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeAdapter(private val context: Home) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var Uids: ArrayList<String> = arrayListOf()
    private val db: FirebaseFirestore = Firebase.firestore
    private val usersCollectionRef = db.collection("users")
    var firestore = Firebase.firestore
    val database = Firebase.database
    var PostDatas=ArrayList<PostData>()
    val friendsRef = database.getReference("friends")
    var friendList: ArrayList<String> = arrayListOf()

    init {
        Uids.clear()
        Firebase.auth.currentUser?.let { friendsRef.child(it.uid) }
            ?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (child in dataSnapshot.children) {
                        friendList.add(child.child("email").value.toString())
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.homeitem, parent, false)
        return CustomViewHolder(view)
        /*val binding = HomeitemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CustomViewHolder(binding)*/
    }

    fun updateList(newList: ArrayList<PostData>) {
        PostDatas = newList
        notifyDataSetChanged()
    }



    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val viewholder = (holder as CustomViewHolder).itemView

        viewholder.userNameText.text = PostDatas!![position].userId
        //이름

        Glide.with(holder.itemView.context).load(PostDatas!![position].imageUrl)
            .into(viewholder.postImage)
        //이미지

        viewholder.postText.text = PostDatas!![position].posttext
        //내용

        viewholder.likeCountText.text = "Likes " + PostDatas!![position].likecount

    }

    override fun getItemCount(): Int {
        return PostDatas.size
    }
}
