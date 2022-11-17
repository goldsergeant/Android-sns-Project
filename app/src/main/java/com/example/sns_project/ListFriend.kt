package com.example.sns_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sns_project.databinding.ActivityListfriendBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFriend.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFriend : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var adapter: MyAdapter? = null
    val database= Firebase.database
    val friendsRef= Firebase.auth.currentUser?.let { database.getReference("friends").child(it.uid) }
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
        val binding=ActivityListfriendBinding.inflate(layoutInflater)
        adapter = MyAdapter(this, emptyList())
        binding.recyclerviewItems.layoutManager=LinearLayoutManager(activity)
        binding.recyclerviewItems.adapter=adapter
        val query= friendsRef?.orderByChild(id.toString())
        if (query != null) {
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val items= mutableListOf<Item>()
                    for (child in dataSnapshot.children) {
                        items.add(Item(child.key ?: "", child.value as Map<*, *>))
                        println("${child.key} - ${child.value as Map<*,*>}")
                    }
                    adapter?.updateList(items)
                }
                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                }
            })
        }



        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListFriend.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListFriend().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}