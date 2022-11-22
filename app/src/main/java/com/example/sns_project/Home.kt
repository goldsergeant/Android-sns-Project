package com.example.sns_project

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.sns_project.databinding.ActivityHomeBinding
import com.example.sns_project.databinding.ActivityPostBinding
import com.example.sns_project.databinding.HomeitemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.homeitem.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var firestore : FirebaseFirestore? = null
    var uid : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = LayoutInflater.from(activity).inflate(R.layout.activity_home,container,false)
        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid

        view.recyclerview_home.adapter = HomeRecyclerViewAdapter()
        view.recyclerview_home.layoutManager = LinearLayoutManager(activity)
        (view.recyclerview_home.layoutManager as LinearLayoutManager).setReverseLayout(true)
        (view.recyclerview_home.layoutManager as LinearLayoutManager).setStackFromEnd(true)

        return view
    }


    inner class HomeRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        var PostDatas : ArrayList<PostData> = arrayListOf()
        var Uids : ArrayList<String> = arrayListOf()

        init {

            firestore?.collection("images")?.orderBy("timestamp")?.addSnapshotListener{ querySnapshot, firebaseFirestoreException ->
                PostDatas.clear()
                Uids.clear()
                for(snapshot in querySnapshot!!.documents){
                    var item = snapshot.toObject(PostData::class.java)
                    PostDatas.add(item!!)
                    Uids.add(snapshot.id)
                }
                notifyDataSetChanged()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.homeitem,parent,false)
            return CustomViewHolder(view)
            /*val binding = HomeitemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return CustomViewHolder(binding)*/
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)


        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewholder = (holder as CustomViewHolder).itemView

            viewholder.userNameText.text = PostDatas!![position].userId
            //이름

            Glide.with(holder.itemView.context).load(PostDatas!![position].imageUrl).into(viewholder.postImage)
            //이미지

            viewholder.postText.text = PostDatas!![position].posttext
            //내용

            viewholder.likeCountText.text = "Likes " + PostDatas!![position].likecount

        }

        override fun getItemCount(): Int {
            return PostDatas.size
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

